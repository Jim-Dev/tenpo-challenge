package com.tenpo.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Calculate request DTO.
 * @param num1 the first number
 * @param num2 the second number
 */
@Schema(description = "Request payload for the calculation endpoint")
public record CalculateRequest(
    @Schema(description = "First number", example = "5")
    @NotNull Double num1,
    @Schema(description = "Second number", example = "5")
    @NotNull Double num2
) {
}