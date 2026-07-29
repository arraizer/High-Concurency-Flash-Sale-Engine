package com.example.flashsale.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.data.annotation.Version;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Table(name = "inventories")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_inventory_product"))
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // getters, setters
}