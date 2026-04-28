package com.tenpo.challenge.config.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ProblemDetail;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that applies rate limiting (3 requests per minute per client IP).
 * Excludes Swagger UI and API docs from rate limiting.
 * Only active when "rate-limit" profile is enabled.
 */
@Component
@Profile("rate-limit")
public final class RateLimitFilter extends OncePerRequestFilter {

    /** Maximum requests per minute per client. */
    private static final int MAX_REQUESTS_PER_MINUTE = 3;

    /** JSON object mapper for error responses. */
    private final ObjectMapper objectMapper;

    /** In-memory store of rate limit buckets per client IP. */
    private final ConcurrentHashMap<String, Bucket> buckets =
        new ConcurrentHashMap<>();

    /** Rate limit configuration: 3 requests per minute. */
    private final Bandwidth limit =
        Bandwidth.simple(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1));

    /**
     * Constructs a new RateLimitFilter.
     * @param givenObjectMapper the JSON object mapper
     */
    public RateLimitFilter(final ObjectMapper givenObjectMapper) {
        this.objectMapper = givenObjectMapper;
    }

    /**
     * Returns or creates a rate limit bucket for the given client IP.
     * @param clientIp the client IP address
     * @return the rate limit bucket for the client
     */
    private Bucket getBucket(final String clientIp) {
        return buckets.computeIfAbsent(clientIp,
            ip -> Bucket.builder().addLimit(limit).build());
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        Bucket bucket = getBucket(clientIp);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "Too many requests. Please try again later."
            );
            problem.setTitle("Too Many Requests");
            objectMapper.writeValue(response.getWriter(), problem);
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/")
                || path.equals("/")
                || path.startsWith("/error");
    }

}
