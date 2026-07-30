package com.example.flashsale.service;

import com.example.flashsale.dto.FlashSaleEventResponseDTO;
import com.example.flashsale.dto.FlashSaleItemResponseDTO;
import com.example.flashsale.entity.FlashSaleEvent;
import com.example.flashsale.entity.FlashSaleItem;
import com.example.flashsale.repository.FlashSaleEventRepository;
import com.example.flashsale.repository.FlashSaleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashSaleService {

    private final FlashSaleEventRepository eventRepository;
    private final FlashSaleItemRepository itemRepository;

    // Lấy danh sách tất cả các đợt Flash Sale
    public List<FlashSaleEventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToEventDTO)
                .collect(Collectors.toList());
    }

    // Lấy danh sách sản phẩm giảm giá thuộc 1 đợt Flash Sale
    public List<FlashSaleItemResponseDTO> getItemsByEventId(Long eventId) {
        // Tìm các item thuộc eventId
        List<FlashSaleItem> items = itemRepository.findByEventId(eventId);
        return items.stream()
                .map(this::mapToItemDTO)
                .collect(Collectors.toList());
    }

    private FlashSaleEventResponseDTO mapToEventDTO(FlashSaleEvent event) {
        return FlashSaleEventResponseDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .status(event.getStatus())
                .build();
    }

    private FlashSaleItemResponseDTO mapToItemDTO(FlashSaleItem item) {
        return FlashSaleItemResponseDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .originalPrice(item.getProduct().getPrice())
                .flashPrice(item.getFlashPrice())
                .stock(item.getStock())
                .soldCount(item.getSoldCount())
                .status(item.getStatus())
                .build();
    }
}