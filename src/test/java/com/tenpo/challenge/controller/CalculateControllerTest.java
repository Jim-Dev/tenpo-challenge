package com.tenpo.challenge.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.tenpo.challenge.dto.CalculateRequest;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;
import com.tenpo.challenge.service.exceptions.ExternalServiceException;


public class CalculateControllerTest {
    @Test
    void calculate_should_throw_exception_if_retry_count_exceeds_limit_and_no_cached_available() {
        CalculateService  calculateService = mock(CalculateService.class);
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        PercentageService percentageService = mock(PercentageService.class);
         CalculateController controller = new CalculateController(
            percentageService,
            calculateService,
            cacheManager
        );
        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));
        when(cacheManager.getCache("percentage")
            .get("percentage", Double.class)).thenReturn(null);

        assertThatThrownBy(() ->
            controller.calculate(new CalculateRequest(5.0, 5.0)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("No cached percentage value available");
    }
}
