package com.example.flashsale.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "flashsale.order.queue";
    public static final String EXCHANGE_NAME = "flashsale.exchange";
    public static final String ROUTING_KEY = "flashsale.order.routingKey";

    @Bean
    public Queue orderQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}