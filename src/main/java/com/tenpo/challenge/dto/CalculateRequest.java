package com.tenpo.challenge.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Calculate request DTO.
 * @param num1 the first number
 * @param num2 the second number
 */
public record CalculateRequest(
    @NotNull Double num1,
    @NotNull Double num2
) {
}