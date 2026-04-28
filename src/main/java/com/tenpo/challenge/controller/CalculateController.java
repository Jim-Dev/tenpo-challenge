package com.tenpo.challenge.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenpo.challenge.dto.CalculateRequest;
import com.tenpo.challenge.dto.CalculateResponse;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Calculate", description = "Calculation API")
@RestController
@RequestMapping("/api")
public final class CalculateController {

    /** Default fallback percentage value. */
    private static final Double DEFAULT_FALLBACK_PERCENTAGE = 10.0;

    /** Service for retrieving percentage values. */
    private final PercentageService percentageService;
    /** Service for performing calculations. */
    private final CalculateService calculateService;
    /** Cache manager for storing percentage values. */
    private final CacheManager cacheManager;

    /**
     * Constructs a new CalculateController with the given services.
     * @param givenPercentageService the service for retrieving
     *                               percentage values
     * @param givenCalculateService the service for performing calculations
     * @param givenCacheManager the cache manager for storing percentage values
     */
    public CalculateController(
        final PercentageService givenPercentageService,
        final CalculateService givenCalculateService,
        final CacheManager givenCacheManager) {
        this.percentageService = givenPercentageService;
        this.calculateService = givenCalculateService;
        this.cacheManager = givenCacheManager;
    }

    /**
     * Calculates the result based on the provided numbers and percentage.
     * @param request the calculate request containing num1 and num2
     * @return ResponseEntity with CalculateResponse
     */
    @Operation(
        summary = "Calculate the result based on num1, num2 and percentage",
        description = "This endpoint calculates the result using the formula:"
            + " (num1 + num2) * (1 + percentage / 100)."
            + " The percentage is retrieved from an external service."
            + " If the service fails, it will attempt to use a cached value."
            + " If no cached value is available, it will throw an error."
    )
    @ApiResponse(responseCode = "200", description = "Successful calculation")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/calculate")
    public ResponseEntity<CalculateResponse> calculate(
        @Valid @RequestBody final CalculateRequest request) {

            Double percentage = 0.0;
            try {
                percentage = percentageService.getPercentage();
                cacheManager.getCache("percentage")
                    .put("percentage", percentage);
            } catch (Exception e) {
                percentage = cacheManager.getCache("percentage")
                    .get("percentage", Double.class);
                if (percentage == null) {
                    throw new IllegalStateException(
                        "No cached percentage value available", e);
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
