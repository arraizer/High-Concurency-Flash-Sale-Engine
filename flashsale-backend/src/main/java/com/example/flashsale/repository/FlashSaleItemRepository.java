package com.example.flashsale.repository;

import com.example.flashsale.entity.FlashSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {
    List<FlashSaleItem> findByEventId(Long eventId);
}