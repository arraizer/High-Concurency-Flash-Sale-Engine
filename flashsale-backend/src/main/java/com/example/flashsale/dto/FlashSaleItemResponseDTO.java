package com.example.flashsale.dto;

import com.example.flashsale.enums.FlashSaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlashSaleItemResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal originalPrice;
    private BigDecimal flashPrice;
    private Integer stock;
    private Integer soldCount;
    private FlashSaleItemStatus status;
}