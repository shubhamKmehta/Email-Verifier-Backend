package com.email_verifier.interceptor;

import com.email_verifier.service.RateLimiterService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String clientIp = request.getRemoteAddr();
        long remaining = rateLimiterService.getRemainingTokens(clientIp);

        // Response headers mein remaining requests batao
        response.addHeader("X-RateLimit-Limit", "5");
        response.addHeader("X-RateLimit-Remaining",
                String.valueOf(remaining));

        if (!rateLimiterService.isAllowed(clientIp)) {
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "error": "Too Many Requests",
                    "message": "5 requests per minute allowed only!",
                    "retryAfter": "60 seconds"
                }
                """);
            return false; // request block!
        }

        return true; // request allow!
    }
}
