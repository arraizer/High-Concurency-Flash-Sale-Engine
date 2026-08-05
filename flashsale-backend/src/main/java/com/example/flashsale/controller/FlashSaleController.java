package com.example.flashsale.controller;

import com.example.flashsale.dto.FlashSaleEventResponseDTO;
import com.example.flashsale.dto.FlashSaleItemResponseDTO;
import com.example.flashsale.dto.FlashSaleOrderMessage;
import com.example.flashsale.service.FlashSaleService;
import com.example.flashsale.service.FlashSaleStockService;
import com.example.flashsale.service.OrderMessageProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public ResponseEntity<String> deductStockTest(@PathVariable Long itemId, @RequestParam(defaultValue = "1") int quantity) {
    boolean success = stockService.deductStock(itemId, quantity);
    
    if (success) {
        // Sau khi trừ kho Redis thành công -> Đẩy message tạo đơn bất đồng bộ
        FlashSaleOrderMessage message = new FlashSaleOrderMessage(101L, itemId, quantity);
        orderMessageProducer.sendOrderMessage(message);

        return ResponseEntity.ok("Đặt hàng thành công! Stock còn lại trên Redis: " + stockService.getStockFromRedis(itemId));
    } else {
        return ResponseEntity.badRequest().body("Trừ kho thất bại: Sản phẩm đã HẾT HÀNG!");
    }
}

}