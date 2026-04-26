package com.tenpo.challenge.service;

import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    
    public Double getResult(Double num1, Double num2, Double percentage) {
        return (num1 + num2) * (1 + percentage / 100);
    }
}
