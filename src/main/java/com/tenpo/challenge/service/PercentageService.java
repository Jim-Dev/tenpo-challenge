package com.tenpo.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.tenpo.challenge.service.exceptions.ExternalServiceException;

/**
 * Service to provide percentage values.
 */
@Service
public class PercentageService {

    /** Maximum limit for percentage. */
    private static final Double MAX_LIMIT = 100.0;
    /** Default failure rate. */
    private static final Double DEFAULT_FAILURE_RATE = 0.25;
    /** Maximum number of retry attempts. */
    private static final int MAX_RETRY_ATTEMPTS = 3;
    /** Delay between retries in milliseconds. */
    private static final long RETRY_DELAY_MS = 500L;

    /** Cache manager instance. */
    private final CacheManager cacheManager;
    /** Current failure rate. */
    private double failureRate;

    /**
     * Constructs a PercentageService.
     * @param givenCacheManager the cache manager
     */
    @Autowired
    public PercentageService(final CacheManager givenCacheManager) {
        this(givenCacheManager, DEFAULT_FAILURE_RATE);
    }

    /**
     * Constructs a PercentageService with a failure rate.
     * @param givenCacheManager the cache manager
     * @param givenFailureRate the failure rate
     */
    public PercentageService(
            final CacheManager givenCacheManager,
            final double givenFailureRate) {
        this.cacheManager = givenCacheManager;
        if (givenFailureRate < 0.0 || givenFailureRate > 1.0) {
            this.failureRate = DEFAULT_FAILURE_RATE;
        } else {
            this.failureRate = givenFailureRate;
        }
    }

    /**
     * Determine if an operation should fail based on the failure rate.
     * @return true if it should fail, false otherwise
     */
    private boolean shouldFail() {
        return Math.random() < failureRate;
    }

    /**
     * Get a percentage value, possibly throwing an exception.
     * @return the percentage
     */
    @Retryable(
            maxAttempts = MAX_RETRY_ATTEMPTS,
            backoff = @Backoff(delay = RETRY_DELAY_MS, multiplier = 2),
            retryFor = ExternalServiceException.class)
    public final Double getPercentage() {

        if (shouldFail()) {
            throw new ExternalServiceException("External service unavailable");
        }
        double randomPercentage = Math.random() * (MAX_LIMIT + 1);
        return (double) Math.round(randomPercentage);
    }

    /**
     * Recover from a failure by returning a cached percentage.
     * @param e the exception that caused the failure
     * @return the cached percentage
     */
    @Recover
    public final Double recover(final ExternalServiceException e) {
        Cache cache = cacheManager.getCache("percentage");
        Double cachedPercentage = cache.get("percentage", Double.class);
        if (cachedPercentage != null) {
            return cachedPercentage;
        } else {
            throw new IllegalStateException(
                "No cached percentage available", e);
        }
    }
}