package com.tenpo.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Error response DTO.
 * @param message the error code
 * @param details the error details
 */
@Schema(description = "Error response returned on failure")
public record ErrorResponse(
        @Schema(description = "Error code", example = "EX001")
        String message,
        @Schema(description = "Error details", example = "No cached percentage value available")
        String details) {
}
