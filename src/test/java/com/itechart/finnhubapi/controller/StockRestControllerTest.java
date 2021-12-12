package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDetailsDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.service.StockService;
import com.itechart.finnhubapi.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class StockRestControllerTest {

    @Mock
    private StockService stockService;
    @Mock
    private UserUtil userUtil;
    @InjectMocks
    private StockRestController stockRestController;

    protected MockMvc mvc;
    private final FinancialStatementDto finance = new FinancialStatementDto();
    private final MetricDto metric = new MetricDto();

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(stockRestController).build();
    }

    @Test
    void financialsWithHighSubscription() throws Exception {
        finance.setSymbol("Test");
        doReturn(3L).when(userUtil).userID(any());
        doReturn(finance).when(stockService).getFinancialResponseEntity(anyString(), anyLong());
        MvcResult result = mvc.perform(get("/api/v1/stock/financials/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getFinancialResponseEntity(anyString(), anyLong());
        verify(userUtil,times(1)).userID(any());
    }

    @Test
    void metricWithMediumSubscription() throws Exception {
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
        doReturn(3L).when(userUtil).userID(any());
        doReturn(metric).when(stockService).getMetricResponseEntity(anyString(), anyLong());
        MvcResult result = mvc.perform(get("/api/v1/stock/metric/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getMetricResponseEntity(anyString(), anyLong());
        verify(userUtil,times(1)).userID(any());
    }
}