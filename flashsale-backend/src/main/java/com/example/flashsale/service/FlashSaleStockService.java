package com.example.flashsale.service;

import java.util.List;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flashsale.entity.FlashSaleItem;
import com.example.flashsale.repository.FlashSaleItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashSaleStockService {

    private final RedissonClient redissonClient;
    private final FlashSaleItemRepository flashSaleItemRepository;

    private static final String STOCK_KEY_PREFIX = "flashsale:item:";
    private static final String STOCK_KEY_SUFFIX = ":stock";

    /**
     * Warm-up toàn bộ tồn kho Flash Sale Item vào Redis khi ứng dụng khởi chạy
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmupStockToRedis() {
        log.info(">>> Starting Stock Warm-up to Redis...");
        List<FlashSaleItem> items = flashSaleItemRepository.findAll();
        for (FlashSaleItem item : items) {
            String key = getStockKey(item.getId());
            RAtomicLong atomicStock = redissonClient.getAtomicLong(key);
            atomicStock.set(item.getStock());
            log.info("Warmed up Redis key: {} with stock: {}", key, item.getStock());
        }
        log.info(">>> Stock Warm-up Completed successfully!");
    }

    /**
     * Trừ kho nguyên tử (Atomic) trên Redis
     * @return true nếu trừ kho thành công, false nếu hết hàng
     */
    public boolean deductStock(Long itemId, int quantity) {
        String key = getStockKey(itemId);
        RAtomicLong atomicStock = redissonClient.getAtomicLong(key);

        // Thao tác atomic decrement
        long remainingStock = atomicStock.addAndGet(-quantity);

        if (remainingStock < 0) {
            // Nếu stock âm -> Hết hàng -> Rollback lại giá trị
            atomicStock.addAndGet(quantity);
            return false;
        }

        return true;
    }

    /**
     * Lấy tồn kho hiện tại từ Redis
     */
    public long getStockFromRedis(Long itemId) {
        String key = getStockKey(itemId);
        RAtomicLong atomicStock = redissonClient.getAtomicLong(key);
        return atomicStock.get();
    }

    private String getStockKey(Long itemId) {
        return STOCK_KEY_PREFIX + itemId + STOCK_KEY_SUFFIX;
    }


    @Transactional
    public boolean deductStockDirectDB(Long itemId, int quantity) {
        // Thực thi câu lệnh UPDATE trực tiếp xuống PostgreSQL
        int updatedRows = flashSaleItemRepository.deductStockDirectDB(itemId, quantity);
        
        // Nếu số dòng bị ảnh hưởng > 0 nghĩa là trừ kho DB thành công!
        return updatedRows > 0;
    }
}