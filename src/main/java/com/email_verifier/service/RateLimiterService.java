package com.email_verifier.service;

import io.github.bucket4j.*;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // Har IP ka alag bucket
    private final ConcurrentHashMap<String, Bucket> buckets
            = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(5)                         // 5 requests
                        .refillIntervally(5, Duration.ofMinutes(1)) // per minute
                        .build())
                .build();
    }

    public boolean isAllowed(String clientIp) {
        Bucket bucket = buckets.computeIfAbsent(
                clientIp, k -> createBucket()
        );
        return bucket.tryConsume(1);
    }

    public long getRemainingTokens(String clientIp) {
        Bucket bucket = buckets.get(clientIp);
        return bucket != null ? bucket.getAvailableTokens() : 5;
    }
}