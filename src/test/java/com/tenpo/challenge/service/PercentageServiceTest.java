package com.tenpo.challenge.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.tenpo.challenge.service.exceptions.ExternalServiceException;

class PercentageServiceTest {

    @Test
    void getPercentage_is_between_0_and_100() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("percentage")).thenReturn(cache);

        PercentageService percentageService = new PercentageService(cacheManager, 0.0);
        Double percentage = percentageService.getPercentage();

        assertTrue(percentage >= 0.0 && percentage <= 100.0);
    }

    @Test
    void recover_returns_cached_value() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(42.0);

        PercentageService percentageService = new PercentageService(cacheManager, 1.0);
        Double result = percentageService.recover(new ExternalServiceException("fail"));

        assertTrue(result.equals(42.0));
    }

    @Test
    void recover_throws_exception_when_no_cache() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(null);

        PercentageService percentageService = new PercentageService(cacheManager, 1.0);
        assertThrows(IllegalStateException.class,
                () -> percentageService.recover(new ExternalServiceException("fail")));
    }
}