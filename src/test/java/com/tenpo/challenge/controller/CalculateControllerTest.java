package com.tenpo.challenge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tenpo.challenge.dto.CalculateRequest;
import com.tenpo.challenge.dto.CalculateResponse;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;
import com.tenpo.challenge.service.exceptions.ExternalServiceException;

class CalculateControllerTest {

    @Test
    void should_cache_percentage_when_service_succeeds() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateService calculateService = mock(CalculateService.class);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(percentageService.getPercentage()).thenReturn(10.0);
        when(calculateService.getResult(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(11.0);

        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        ResponseEntity<CalculateResponse> response = 
            controller.calculate(new CalculateRequest(5.0, 5.0));

        assertThat(response.getBody().result()).isEqualTo(11.0);
        verify(cache).put("percentage", 10.0);
    }

    @Test
    void should_get_from_cache_when_service_fails() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateService calculateService = mock(CalculateService.class);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));
        when(cache.get("percentage", Double.class)).thenReturn(15.0);
        when(calculateService.getResult(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(12.0);

        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        ResponseEntity<CalculateResponse> response = 
            controller.calculate(new CalculateRequest(5.0, 5.0));

        assertThat(response.getBody().percentage()).isEqualTo(15.0);
        verify(cache, never()).put(anyDouble(), anyDouble());
    }

    @Test
    void should_throw_when_service_fails_and_no_cache() {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateService calculateService = mock(CalculateService.class);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(null);
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));

        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        assertThatThrownBy(() -> 
            controller.calculate(new CalculateRequest(5.0, 5.0)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("No cached percentage");
    }
}
