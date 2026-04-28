package com.tenpo.challenge.controller;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tenpo.challenge.repository.entity.CallHistory;
import com.tenpo.challenge.service.CallHistoryService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CallHistoryService callHistoryService;

    @BeforeEach
    void setUp() {
        CallHistory entry1 = new CallHistory(
            Instant.now(),
            "/api/calculate",
            "{\"num1\": 10, \"num2\": 20}",
            "{\"result\": 36.0}",
            null
        );

        CallHistory entry2 = new CallHistory(
            Instant.now(),
            "/api/calculate",
            "{\"num1\": 5, \"num2\": 15}",
            null,
            "HTTP 500: Internal Server Error"
        );

        Mockito.when(callHistoryService.getCallHistory(anyInt(), anyInt()))
            .thenReturn(List.of(entry1, entry2));
        Mockito.when(callHistoryService.countCallHistory()).thenReturn(2L);
    }

    @Test
    void getHistory_shouldReturnPaginatedResults() throws Exception {
        mockMvc.perform(get("/api/history")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void getHistory_withDefaultParams_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/history"))
            .andExpect(status().isOk());
    }

    @Test
    void getHistory_withInvalidPage_shouldFail() throws Exception {
        mockMvc.perform(get("/api/history")
            .param("page", "-1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getHistory_withTooLargeSize_shouldFail() throws Exception {
        mockMvc.perform(get("/api/history")
            .param("size", "101"))
            .andExpect(status().isBadRequest());
    }
}
