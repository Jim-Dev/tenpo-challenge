package com.tenpo.challenge.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic paginated response DTO.
 *
 * @param <T> the type of content
 * @param content the list of items in current page
 * @param page the current page number (0-indexed)
 * @param size the page size
 * @param totalElements the total number of items
 * @param totalPages the total number of pages
 * @param first whether this is the first page
 * @param last whether this is the last page
 */
@Schema(description = "Paginated response wrapper")
public record PageResponse<T>(

    @Schema(description = "List of items in current page")
    List<T> content,

    @Schema(description = "Current page number (0-indexed)",
        example = "0")
    int page,

    @Schema(description = "Page size", example = "10")
    int size,

    @Schema(description = "Total number of items", example = "100")
    long totalElements,

    @Schema(description = "Total number of pages", example = "10")
    int totalPages,

    @Schema(description = "Whether this is the first page",
        example = "true")
    boolean first,

    @Schema(description = "Whether this is the last page",
        example = "false")
    boolean last

) {
}
