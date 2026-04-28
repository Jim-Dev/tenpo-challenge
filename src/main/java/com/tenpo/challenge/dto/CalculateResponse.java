package com.tenpo.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Calculate response DTO.
 * @param num1 the first number
 * @param num2 the second number
 * @param percentage the percentage
 * @param result the result
 */
@Schema(description = "Response payload with the calculation result")
public record CalculateResponse(
    @Schema(description = "First number", example = "5")
    @NotNull Double num1,
    @Schema(description = "Second number", example = "5")
    @NotNull Double num2,
    @Schema(description = "Percentage applied", example = "10")
    @NotNull Double percentage,
    @Schema(
        description = "Calculated result:"
            + " (num1 + num2) * (1 + percentage / 100)",
        example = "11"
    )
    @NotNull Double result
) {
}
