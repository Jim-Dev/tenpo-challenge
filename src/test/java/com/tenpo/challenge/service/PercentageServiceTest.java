package com.tenpo.challenge.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

class PercentageServiceTest {
    
    @Test
    void getPercentage_is_between_0_and_100() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("percentage")).thenReturn(cache);
        
        PercentageService percentageService = new PercentageService(cacheManager, 0.0);
        Double percentage = percentageService.getPercentage();

        assert percentage >= 0.0 && percentage <= 100.0;
    }
}