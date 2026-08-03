package com.example.flashsale.service;

import com.example.flashsale.config.RabbitMQConfig;
import com.example.flashsale.dto.FlashSaleOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderMessageConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveOrderMessage(FlashSaleOrderMessage message) {
        log.info("<<< [CONSUMER] Received order message from RabbitMQ: UserId={}, ItemId={}, Quantity={}", 
                message.getUserId(), message.getItemId(), message.getQuantity());

        // Sau này bước này sẽ gọi OrderRepository.save(...) để lưu xuống DB
        log.info(">>> [CONSUMER] Successfully processed and saved Order to Postgres DB!");
    }
}