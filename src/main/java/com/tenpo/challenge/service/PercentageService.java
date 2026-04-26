package com.tenpo.challenge.service;

import org.springframework.stereotype.Service;

@Service
public class PercentageService {

    private static final Double MAX_LIMIT = 100.0;
    
    public Double getPercentage() {
        double randomPercentage = Math.random() * (MAX_LIMIT+1);
        return (double) Math.round(randomPercentage);
    }
}