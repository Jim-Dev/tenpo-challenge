package com.tenpo.challenge.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for call history entries.
 *
 * @param id the unique identifier
 * @param timestamp the timestamp of the call
 * @param endpoint the API endpoint called
 * @param parameters the request parameters
 * @param response the response body
 * @param error the error message if any
 */
@Schema(description = "Call history entry response")
public record CallHistoryResponse(

    @Schema(description = "Unique identifier", example = "1")
    Long id,

    @Schema(description = "Timestamp of the call",
        example = "2026-04-28T15:00:00Z")
    Instant timestamp,

    @Schema(description = "API endpoint called",
        example = "/api/calculate")
    String endpoint,

    @Schema(description = "Request parameters",
        example = "{\"num1\": 10, \"num2\": 20}")
    String parameters,

    @Schema(description = "Response body",
        example = "{\"result\": 36.0}")
    String response,

    @Schema(description = "Error message if any",
        example = "HTTP 500: Internal Server Error")
    String error

) {

    /**
     * Converts a CallHistory entity to a CallHistoryResponse DTO.
     *
     * @param entity the CallHistory entity
     * @return the CallHistoryResponse DTO
     */
    public static CallHistoryResponse fromEntity(
        final com.tenpo.challenge.repository.entity.CallHistory entity) {
        return new CallHistoryResponse(
            entity.getId(),
            entity.getTimestamp(),
            entity.getEndpoint(),
            entity.getParameters(),
            entity.getResponse(),
            entity.getError()
        );
    }
}
