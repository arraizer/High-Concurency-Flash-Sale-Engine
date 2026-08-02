package com.example.flashsale.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host:choice-marlin-177093.upstash.io}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:gQAAAAAAArPFAAIgcDE5M2E0YWZlM2ViM2I0MGI5YjVlMzg5M2EwOWEyMWU2Yg}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 1. Chỉ truyền protocol + host + port đơn thuần vào Address (Dùng rediss:// cho SSL)
        String address = String.format("rediss://%s:%d", redisHost, redisPort);

        config.useSingleServer()
                .setAddress(address)
                .setUsername("default")      // 2. Set Username chuẩn của Upstash
                .setPassword(redisPassword); // 3. Redisson sẽ tự encode password an toàn tuyệt đối!

        return Redisson.create(config);
    }
}