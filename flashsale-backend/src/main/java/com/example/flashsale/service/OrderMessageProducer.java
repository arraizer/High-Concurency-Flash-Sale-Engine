package com.example.flashsale.service;

import com.example.flashsale.config.RabbitMQConfig;
import com.example.flashsale.dto.FlashSaleOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderMessageProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderMessage(FlashSaleOrderMessage message) {
        log.info(">>> [PRODUCER] Sending order message to RabbitMQ: UserId={}, ItemId={}", 
                message.getUserId(), message.getItemId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}