package com.example.flashsale.entity;

@Entity
@Table(name = "flash_sale_items")
public class FlashSaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_flash_sale_item_event"))
    private FlashSaleEvent event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_flash_sale_item_product"))
    private Product product;

    @Column(name = "flash_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal flashPrice;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "sold_count", nullable = false)
    private Integer soldCount = 0;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FlashSaleItemStatus status = FlashSaleItemStatus.IN_STOCK;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // getters, setters
}
