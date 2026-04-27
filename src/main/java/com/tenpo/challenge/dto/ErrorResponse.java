package com.tenpo.challenge.dto;

public record ErrorResponse(
        String message,
        String details) {
}
