package com.example.flashsale.repository;

import com.example.flashsale.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    // Tìm các event chưa xử lý để gõ worker đẩy sang RabbitMQ
    List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc();
}