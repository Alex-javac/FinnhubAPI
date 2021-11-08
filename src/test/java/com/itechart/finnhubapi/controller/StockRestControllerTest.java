package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDetailsDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.RoleEntity;
import com.itechart.finnhubapi.model.Subscription;
import com.itechart.finnhubapi.model.SubscriptionEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.StockService;
import com.itechart.finnhubapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
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
    void financialsWithLowSubscription() throws Exception {
        finance.setSymbol("Test");
        ResponseEntity<FinancialStatementDto> financialStatementDtoResponseEntity = new ResponseEntity<>(finance, HttpStatus.FORBIDDEN);
        doReturn(financialStatementDtoResponseEntity).when(stockService).getFinancialResponseEntity(anyString());
        MvcResult result = mvc.perform(get("/api/v1/stock/financials/{symbol}", "Test"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getFinancialResponseEntity(anyString());
    }

    @Test
    void financialsWithHighSubscription() throws Exception {
        finance.setSymbol("Test");
        ResponseEntity<FinancialStatementDto> financialStatementDtoResponseEntity = new ResponseEntity<>(finance, HttpStatus.OK);
        doReturn(financialStatementDtoResponseEntity).when(stockService).getFinancialResponseEntity(anyString());
        MvcResult result = mvc.perform(get("/api/v1/stock/financials/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getFinancialResponseEntity(anyString());
    }

    @Test
    void metricWithLowSubscription() throws Exception {
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
        ResponseEntity<MetricDto> metricDtoResponseEntity = new ResponseEntity<>(metric, HttpStatus.FORBIDDEN);
        doReturn(metricDtoResponseEntity).when(stockService).getMetricResponseEntity(anyString());
        MvcResult result = mvc.perform(get("/api/v1/stock/metric/{symbol}", "Test"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getMetricResponseEntity(anyString());
    }

    @Test
    void metricWithMediumSubscription() throws Exception {
        MetricDetailsDto metricDetailsDto = new MetricDetailsDto();
        metric.setMetricDetails(metricDetailsDto);
        ResponseEntity<MetricDto> metricDtoResponseEntity = new ResponseEntity<>(metric, HttpStatus.OK);
        doReturn(metricDtoResponseEntity).when(stockService).getMetricResponseEntity(anyString());
        MvcResult result = mvc.perform(get("/api/v1/stock/metric/{symbol}", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(stockService, times(1)).getMetricResponseEntity(anyString());
    }
}