package com.example.flashsale.controller;

import com.example.flashsale.dto.FlashSaleEventResponseDTO;
import com.example.flashsale.dto.FlashSaleItemResponseDTO;
import com.example.flashsale.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {

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
}