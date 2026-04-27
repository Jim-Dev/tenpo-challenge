package com.tenpo.challenge.service;

import org.springframework.stereotype.Service;

/**
 * Service to calculate result.
 */
@Service
public class CalculateService {

    /** Constant for percentage calculation. */
    private static final double FULL_PERCENTAGE = 100.0;

    /**
     * Get the calculated result.
     *
     * @param num1       the first number
     * @param num2       the second number
     * @param percentage the percentage
     * @return the result
     */
    public final Double getResult(
            final Double num1, final Double num2, final Double percentage) {
        return (num1 + num2) * (1 + percentage / FULL_PERCENTAGE);
    }
}
