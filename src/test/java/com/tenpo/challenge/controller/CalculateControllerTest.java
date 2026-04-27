package com.tenpo.challenge.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

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


public class CalculateControllerTest {
    @Test
    void calculate_should_throw_exception_when_service_fails_and_no_cache() {
        CalculateService calculateService = mock(CalculateService.class);
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(null);
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));

        assertThatThrownBy(() ->
            controller.calculate(new CalculateRequest(5.0, 5.0)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("No cached percentage value available");
    }

    @Test
    void calculate_should_return_result_when_service_succeeds() {
        CalculateService calculateService = mock(CalculateService.class);
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(percentageService.getPercentage()).thenReturn(10.0);
        when(calculateService.getResult(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(11.0);

        ResponseEntity<CalculateResponse> response =
            controller.calculate(new CalculateRequest(5.0, 5.0));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().result()).isEqualTo(11.0);
        verify(cache).put("percentage", 10.0);
    }

    @Test
    void calculate_should_use_cache_when_service_fails_and_cache_exists() {
        CalculateService calculateService = mock(CalculateService.class);
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
        CalculateController controller = new CalculateController(
            percentageService, calculateService, cacheManager);

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(10.0);
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));
        when(calculateService.getResult(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(11.0);

        ResponseEntity<CalculateResponse> response =
            controller.calculate(new CalculateRequest(5.0, 5.0));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().result()).isEqualTo(11.0);
        verify(cache, never()).put(any(), any());
    }
}
