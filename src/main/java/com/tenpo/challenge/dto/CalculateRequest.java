package com.tenpo.challenge.dto;

import jakarta.validation.constraints.NotNull;

public record CalculateRequest(
    @NotNull Double num1,
    @NotNull Double num2
    ) {}