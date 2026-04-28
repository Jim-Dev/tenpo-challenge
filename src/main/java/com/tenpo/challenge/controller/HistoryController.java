package com.tenpo.challenge.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenpo.challenge.dto.CallHistoryResponse;
import com.tenpo.challenge.dto.ErrorResponse;
import com.tenpo.challenge.dto.PageResponse;
import com.tenpo.challenge.service.CallHistoryService;

/**
 * REST controller for retrieving call history.
 */
@Slf4j
@RestController
@RequestMapping("/api/history")
@Tag(name = "History", description = "Call history endpoints")
@Validated
public class HistoryController {

    /** Default page number. */
    private static final int DEFAULT_PAGE = 0;

    /** Default page size. */
    private static final int DEFAULT_SIZE = 10;

    /** Maximum allowed page size. */
    private static final int MAX_PAGE_SIZE = 100;

    /** Service for managing call history. */
    private final CallHistoryService callHistoryService;

    /**
     * Constructs HistoryController with required services.
     *
     * @param givenCallHistoryService the call history service
     */
    public HistoryController(
        final CallHistoryService givenCallHistoryService) {
        this.callHistoryService = givenCallHistoryService;
    }

    /**
     * Retrieves paginated call history entries.
     *
     * @param page the page number (0-indexed), defaults to 0
     * @param size the page size, defaults to 10, max 100
     * @return paginated list of call history entries
     */
    @GetMapping
    @Operation(
        summary = "Get call history",
        description = "Retrieves paginated call history entries ordered by"
            + " timestamp descending",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved call history",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = PageResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid pagination parameters",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<PageResponse<CallHistoryResponse>> getHistory(
        @Parameter(description = "Page number (0-indexed)")
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "Page number must be >= 0")
        final Integer page,

        @Parameter(description = "Page size (max 100)")
        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "Page size must be >= 1")
        @Max(value = MAX_PAGE_SIZE, message = "Page size must be <= 100")
        final Integer size
    ) {
        int pageNum = page != null ? page : DEFAULT_PAGE;
        int pageSize = size != null ? size : DEFAULT_SIZE;

        log.info("Retrieving call history: page={}, size={}",
            pageNum, pageSize);

        List<CallHistoryResponse> content = callHistoryService
            .getCallHistory(pageNum, pageSize)
            .stream()
            .map(CallHistoryResponse::fromEntity)
            .toList();

        long totalElements = callHistoryService.countCallHistory();
        int totalPages = (int) Math.ceil(
            (double) totalElements / pageSize
        );

        boolean first = pageNum == 0;
        boolean last = pageNum >= totalPages - 1;

        PageResponse<CallHistoryResponse> response =
            new PageResponse<>(
                content,
                pageNum,
                pageSize,
                totalElements,
                totalPages,
                first,
                last
            );

        return ResponseEntity.ok(response);
    }
}
