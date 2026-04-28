package com.tenpo.challenge.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tenpo.challenge.repository.CallHistoryRepository;
import com.tenpo.challenge.repository.entity.CallHistory;

/**
 * Service for managing call history entries.
 */
@Service
public final class CallHistoryService {

    /** Repository for call history entries. */
    private final CallHistoryRepository callHistoryRepository;

    /**
     * Constructs CallHistoryService with repository.
     *
     * @param givenHistoryRepository the repository for call history entries
     */
    public CallHistoryService(
        final CallHistoryRepository givenHistoryRepository) {
        this.callHistoryRepository = givenHistoryRepository;
    }

    /**
     * Logs a call in the history asynchronously.
     * Errors are caught and logged to prevent disruption of main flow.
     *
     * @param endpoint the API endpoint
     * @param parameters the request parameters
     * @param response the response
     * @param error the error message
     */
    @Async
    public void logCall(
        final String endpoint,
        final String parameters,
        final String response,
        final String error) {
        try {
            CallHistory callHistory = new CallHistory(
            Instant.now(),
            endpoint,
            parameters,
            response,
            error
        );
        callHistoryRepository.save(callHistory);

        } catch (Exception e) {
            // Log error
        }

    }
}
