package com.tenpo.challenge.service;

import org.springframework.stereotype.Service;

@Service
public class PercentageService {

    private static final Double MOCK_PERCENTAGE = 10.0;

    public Double getPercentage() {
        return MOCK_PERCENTAGE;
    }
}