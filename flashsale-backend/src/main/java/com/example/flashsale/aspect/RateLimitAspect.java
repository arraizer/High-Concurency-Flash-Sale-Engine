package com.example.flashsale.aspect;

import com.example.flashsale.annotation.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(rateLimit)")
    public Object interceptRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        
        // Nhận diện người dùng dựa trên địa chỉ IP
        String clientIp = getClientIp(request);
        String limitKey = rateLimit.key() + ":" + clientIp;

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limitKey);
        
        // Thiết lập cấu hình Rate Limit trên Redis nếu key chưa tồn tại
        // OVERALL: Áp dụng chung cho key này
        rateLimiter.trySetRate(RateType.OVERALL, rateLimit.limit(), rateLimit.timeoutInSeconds(), RateIntervalUnit.SECONDS);

        // Báo Redis kiểm tra: Thử lấy 1 Token
        if (!rateLimiter.tryAcquire(1)) {
            // Vượt quá giới hạn -> Trả về lỗi 429 Too Many Requests lập tức!
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("⚠️ Bạn thao tác quá nhanh! Vui lòng thử lại sau giây lát.");
        }

        // Nếu hợp lệ -> Cho phép đi tiếp vào Controller xử lý
        return joinPoint.proceed();
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}