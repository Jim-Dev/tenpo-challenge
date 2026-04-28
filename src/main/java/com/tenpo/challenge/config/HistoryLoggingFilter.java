package com.tenpo.challenge.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.service.CallHistoryService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Filter that logs API calls to the database asynchronously.
 * Only active when 'history' profile is active.
 */
@Component
@Profile("!test")  // Disable in test profile
public class HistoryLoggingFilter extends OncePerRequestFilter {

    /** Status code threshold for error responses. */
    private static final int ERROR_STATUS_THRESHOLD = 400;

    /** Service for logging call history asynchronously. */
    private final CallHistoryService callHistoryService;

    /** Jackson object mapper for JSON processing. */
    private final ObjectMapper objectMapper;

    /**
     * Constructs HistoryLoggingFilter with required dependencies.
     *
     * @param givenCallHistoryService the call history service (lazy-loaded)
     * @param givenObjectMapper the Jackson object mapper
     */
    public HistoryLoggingFilter(
            @Lazy final CallHistoryService givenCallHistoryService,
            final ObjectMapper givenObjectMapper) {
        this.callHistoryService = givenCallHistoryService;
        this.objectMapper = givenObjectMapper;
    }

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * Logs the request details to the database asynchronously.
     *
     * @param request the wrapped HTTP request
     * @param response the wrapped HTTP response
     */
    private void logRequest(
            final ContentCachingRequestWrapper request,
            final ContentCachingResponseWrapper response) {

        try {
            String endpoint = request.getRequestURI();
            String parameters = new String(
                request.getContentAsByteArray(), StandardCharsets.UTF_8
            );
            if (parameters == null || parameters.isEmpty()) {
                parameters = request.getQueryString();
            }

            byte[] responseBytes = response.getContentAsByteArray();
            String responseBody = new String(
                responseBytes, StandardCharsets.UTF_8
            );
            String error = null;

            if (response.getStatus() >= ERROR_STATUS_THRESHOLD) {
                error = "HTTP " + response.getStatus()
                    + ": " + responseBody;
                responseBody = null;
            }

            try {
                callHistoryService.logCall(
                    endpoint, parameters, responseBody, error
                );
            } catch (Exception e) { }
        } catch (Exception e) { }
    }

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    protected boolean shouldNotFilter(
            final HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/favicon")
                || path.startsWith("/h2-console")
                || path.equals("/")
                || path.startsWith("/error")
                || path.startsWith("/api/history");
    }
}
