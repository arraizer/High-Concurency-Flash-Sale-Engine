package com.example.flashsale.entity;

import java.math.BigDecimal;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_order_item_order"))
    @JsonIgnore // prevent serialization loop
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_order_item_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flash_sale_item_id", nullable = true,
                foreignKey = @ForeignKey(name = "fk_order_item_flash_sale"))
    private FlashSaleItem flashSaleItem;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // getters, setters
}