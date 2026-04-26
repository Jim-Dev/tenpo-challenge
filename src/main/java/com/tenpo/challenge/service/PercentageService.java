package com.tenpo.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.tenpo.challenge.service.exceptions.ExternalServiceException;

@Service
public class PercentageService {

    private final CacheManager cacheManager;
    private static final Double MAX_LIMIT = 100.0;
    private static final Double DEFAULT_FAILURE_RATE = 0.25;
    private double failureRate;

    @Autowired
    public PercentageService(CacheManager cacheManager) {
        this(cacheManager, DEFAULT_FAILURE_RATE);
    }
    
    public PercentageService(CacheManager cacheManager, double failureRate) {
        this.cacheManager = cacheManager;
        if (failureRate < 0.0 || failureRate > 1.0)
            this.failureRate = DEFAULT_FAILURE_RATE;
        else
            this.failureRate = failureRate;
    }
    
    private boolean shouldFail() {
        return Math.random() < failureRate;
    }

    @Retryable(
        maxAttempts = 3,
        backoff = @Backoff(delay = 500, multiplier = 2),
        retryFor = ExternalServiceException.class
    )
    public Double getPercentage() {

        if (shouldFail()) {
            throw new ExternalServiceException("External service unavailable");
        }
        double randomPercentage = Math.random() * (MAX_LIMIT+1);
        return (double) Math.round(randomPercentage);
    }

    @Recover
    public Double recover(ExternalServiceException e) {
        Cache cache = cacheManager.getCache("percentage");
        Double cachedPercentage = cache.get("percentage", Double.class);
        if (cachedPercentage != null) {
            return cachedPercentage;
        }else 
            throw new IllegalStateException("No cached percentage available", e);
    }
}