package com.example.flashsale.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    // Prefix cho Key lưu trên Redis
    String key() default "rate_limit";
    
    // Số request tối đa cho phép trong khung thời gian
    long limit() default 5;
    
    // Thời gian tính bằng giây (Mặc định: 1 giây)
    long timeoutInSeconds() default 1;
}