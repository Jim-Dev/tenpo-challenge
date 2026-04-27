package com.tenpo.challenge.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Calculate response DTO.
 * @param num1 the first number
 * @param num2 the second number
 * @param percentage the percentage
 * @param result the result
 */
public record CalculateResponse(
    @NotNull Double num1,
    @NotNull Double num2,
    @NotNull Double percentage,
    @NotNull Double result
) {
}