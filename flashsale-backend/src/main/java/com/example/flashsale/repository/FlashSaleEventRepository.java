package com.example.flashsale.repository;

import com.example.flashsale.entity.FlashSaleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashSaleEventRepository extends JpaRepository<FlashSaleEvent, Long> {
}