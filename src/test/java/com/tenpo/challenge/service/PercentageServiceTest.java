package com.tenpo.challenge.service;

import org.junit.jupiter.api.Test;

class PercentageServiceTest {
    
    @Test
    void getPercentage_is_between_0_and_100() {
        PercentageService percentageService = new PercentageService();
        Double percentage = percentageService.getPercentage();

        assert(percentage >= 0.0 && percentage <= 100.0);
    }
}
