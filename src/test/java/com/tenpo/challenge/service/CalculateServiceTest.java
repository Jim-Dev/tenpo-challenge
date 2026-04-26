package com.tenpo.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CalculateServiceTest {
    
    @Test
    void calculate_result_is_correct() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 5.0;
        Double num2 = 5.0;
        Double percentage = 10.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 11.0;
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void calculate_result_is_correct_with_zero() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 0.0;
        Double num2 = 0.0;
        Double percentage = 10.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 0.0;
        assertEquals(EXPECTED_RESULT, result);
    }
    @Test
    void calculate_result_is_correct_with_negative_numbers() {
        CalculateService calculateService = new CalculateService();
        Double num1 = -5.0;
        Double num2 = -5.0;
        Double percentage = 10.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = -11.0;
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void calculate_result_is_correct_with_large_numbers() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 1_000_000.0;
        Double num2 = 1_000_000.0;
        Double percentage = 10.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 2_200_000.0;
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void calculate_result_is_correct_with_decimal_numbers() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 5.5;
        Double num2 = 4.5;
        Double percentage = 10.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 11.0;
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void calculate_result_is_correct_with_zero_percentage() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 5.0;
        Double num2 = 5.0;
        Double percentage = 0.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 10.0;
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void calculate_result_is_correct_with_hundred_percentage() {
        CalculateService calculateService = new CalculateService();
        Double num1 = 5.0;
        Double num2 = 5.0;
        Double percentage = 100.0;

        Double result = calculateService.getResult(num1, num2, percentage);

        Double EXPECTED_RESULT = 20.0;
        assertEquals(EXPECTED_RESULT, result);
    }
}
