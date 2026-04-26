package com.tenpo.challenge.dto;

import jakarta.validation.constraints.NotNull;

public record CalculateResponse(
    @NotNull Double num1,
    @NotNull Double num2,
    @NotNull Double percentage,
    @NotNull Double result
) {}