package com.example.flashsale.controller;

import com.example.flashsale.dto.FlashSaleEventResponseDTO;
import com.example.flashsale.dto.FlashSaleItemResponseDTO;
import com.example.flashsale.service.FlashSaleService;
import com.example.flashsale.service.FlashSaleStockService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleStockService stockService;
    private final FlashSaleService flashSaleService;

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
    public ResponseEntity<String> deductStockTest(@PathVariable Long itemId,
            @RequestParam(defaultValue = "1") int quantity) {
        boolean success = stockService.deductStock(itemId, quantity);
        if (success) {
            return ResponseEntity
                    .ok("Trừ kho Redis thành công! Stock còn lại: " + stockService.getStockFromRedis(itemId));
        } else {
            return ResponseEntity.badRequest().body("Trừ kho thất bại: Sản phẩm đã HẾT HÀNG!");
        }
    }

}