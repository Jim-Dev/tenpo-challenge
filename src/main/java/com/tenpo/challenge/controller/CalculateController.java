package com.tenpo.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenpo.challenge.dto.CalculateRequest;
import com.tenpo.challenge.dto.CalculateResponse;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CalculateController {

    private final PercentageService percentageService;
    private final CalculateService calculateService;

    public CalculateController(PercentageService percentageService, CalculateService calculateService) {
        this.percentageService = percentageService;
        this.calculateService = calculateService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResponse> calculate(
        @Valid @RequestBody CalculateRequest request ) {

        Double num1 = request.num1();
        Double num2 = request.num2();
        Double percentage = percentageService.getPercentage();
        Double result = calculateService.getResult(num1, num2, percentage);
        CalculateResponse response = new CalculateResponse(
            num1, num2, percentage, result);
        return ResponseEntity.ok(response);
    }
}
