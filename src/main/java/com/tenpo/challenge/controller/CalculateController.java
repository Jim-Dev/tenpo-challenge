package com.tenpo.challenge.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenpo.challenge.config.CacheConfig;
import com.tenpo.challenge.dto.CalculateRequest;
import com.tenpo.challenge.dto.CalculateResponse;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CalculateController {

    private static final int MAX_RETRIES = 3;
    private static final Double DEFAULT_FALLBACK_PERCENTAGE = 10.0;

    private final PercentageService percentageService;
    private final CalculateService calculateService;
    private final CacheManager cacheManager;

    public CalculateController(PercentageService percentageService, CalculateService calculateService, CacheManager cacheManager) {
        this.percentageService = percentageService;
        this.calculateService = calculateService;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResponse> calculate(
        @Valid @RequestBody CalculateRequest request ) {

            Double percentage = 0.0;
            try {
                percentage = percentageService.getPercentage();
                cacheManager.getCache("percentage").put("percentage", percentage);
            } catch (Exception e) {
                percentage = cacheManager.getCache("percentage").get("percentage", Double.class);
                if (percentage == null) {
                    percentage = DEFAULT_FALLBACK_PERCENTAGE;
                }
            }
        Double num1 = request.num1();
        Double num2 = request.num2();
        Double result = calculateService.getResult(num1, num2, percentage);
        CalculateResponse response = new CalculateResponse(
            num1, num2, percentage, result);
        return ResponseEntity.ok(response);
    }
}
