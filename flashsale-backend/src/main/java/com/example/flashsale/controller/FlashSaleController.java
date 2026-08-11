package com.example.flashsale.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.flashsale.annotation.RateLimit;
import com.example.flashsale.dto.FlashSaleEventResponseDTO;
import com.example.flashsale.dto.FlashSaleItemResponseDTO;
import com.example.flashsale.dto.FlashSaleOrderMessage;
import com.example.flashsale.service.FlashSaleService;
import com.example.flashsale.service.FlashSaleStockService;
import com.example.flashsale.service.OrderMessageProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flash-sales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FlashSaleController {
    private final FlashSaleStockService stockService;
    private final FlashSaleService flashSaleService;

    @Autowired
    private OrderMessageProducer orderMessageProducer;

    // API: Lấy danh sách tất cả các đợt Flash Sale
    @GetMapping
    public ResponseEntity<List<FlashSaleEventResponseDTO>> getAllEvents() {
        return ResponseEntity.ok(flashSaleService.getAllEvents());
    }

    // API: Lấy danh sách sản phẩm giảm giá của đợt Flash Sale theo eventId
    @GetMapping("/{eventId}/items")
    public ResponseEntity<List<FlashSaleItemResponseDTO>> getEventItems(@PathVariable Long eventId) {
        return ResponseEntity.ok(flashSaleService.getItemsByEventId(eventId));
    }

    @GetMapping("/items/{itemId}/redis-stock")
    public ResponseEntity<Long> getRedisStock(@PathVariable Long itemId) {
        return ResponseEntity.ok(stockService.getStockFromRedis(itemId));
    }
   @GetMapping("/items/{itemId}/deduct-test")
// Bỏ bớt @RateLimit hoặc tăng nhẹ để test không bị chặn nhầm, ví dụ: limit = 10
@RateLimit(limit = 10, timeoutInSeconds = 1) 
public ResponseEntity<?> deductStockTest(@PathVariable Long itemId, @RequestParam(defaultValue = "1") int quantity) {
    boolean success = stockService.deductStock(itemId, quantity);
    
    Map<String, Object> response = new HashMap<>();

    if (success) {
        FlashSaleOrderMessage message = new FlashSaleOrderMessage(101L, itemId, quantity);
        orderMessageProducer.sendOrderMessage(message);

        response.put("success", true);
        response.put("message", "Đặt hàng thành công!");
        response.put("remainingStock", stockService.getStockFromRedis(itemId));
        return ResponseEntity.ok(response);
    } else {
        response.put("success", false);
        response.put("message", "Trừ kho thất bại: Sản phẩm đã HẾT HÀNG!");
        return ResponseEntity.badRequest().body(response);
    }
}
    // ⚠️ API TEST SYNC: Chọc thẳng Database (Không qua Redis, không qua RabbitMQ, không Rate Limit)
    @GetMapping("/items/{itemId}/deduct-sync-test")
    public ResponseEntity<String> deductStockSyncTest(@PathVariable Long itemId, @RequestParam(defaultValue = "1") int quantity) {
        // Hàm này gọi direct SQL: UPDATE items SET stock = stock - quantity WHERE id = itemId AND stock >= quantity
        boolean success = stockService.deductStockDirectDB(itemId, quantity);
        
        if (success) {
            return ResponseEntity.ok("Trừ kho DB trực tiếp thành công!");
        } else {
            return ResponseEntity.badRequest().body("Trừ kho thất bại: Hết hàng hoặc tranh chấp DB!");
        }
    }

}