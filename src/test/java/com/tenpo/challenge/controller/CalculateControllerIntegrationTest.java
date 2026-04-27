package com.tenpo.challenge.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.context.annotation.Import;
import com.tenpo.challenge.service.CalculateService;
import com.tenpo.challenge.service.PercentageService;
import com.tenpo.challenge.service.exceptions.ExternalServiceException;

@WebMvcTest(CalculateController.class)
@Import(GlobalExceptionHandler.class)
class CalculateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PercentageService percentageService;

    @MockBean
    private CalculateService calculateService;

    @Test
    void should_return_200_when_service_succeeds() throws Exception {
        when(percentageService.getPercentage()).thenReturn(10.0);
        when(calculateService.getResult(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(11.0);

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"num1\": 5.0, \"num2\": 5.0}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(11.0))
            .andExpect(jsonPath("$.percentage").value(10.0));
    }

    @Test
    void should_return_500_when_service_fails_and_no_cache() throws Exception {
        when(percentageService.getPercentage())
            .thenThrow(new ExternalServiceException("fail"));

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"num1\": 5.0, \"num2\": 5.0}"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void should_return_400_when_request_invalid() throws Exception {
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"num1\": null, \"num2\": 5.0}"))
            .andExpect(status().isBadRequest());
    }
}