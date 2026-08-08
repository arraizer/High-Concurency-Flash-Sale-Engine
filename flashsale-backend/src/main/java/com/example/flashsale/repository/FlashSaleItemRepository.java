package com.example.flashsale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.flashsale.entity.FlashSaleItem;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {
    List<FlashSaleItem> findByEventId(Long eventId);
    @Modifying
    @Query("UPDATE FlashSaleItem i SET i.stock = i.stock - :quantity WHERE i.id = :itemId AND i.stock >= :quantity")
    int deductStockDirectDB(@Param("itemId") Long itemId, @Param("quantity") int quantity);
}