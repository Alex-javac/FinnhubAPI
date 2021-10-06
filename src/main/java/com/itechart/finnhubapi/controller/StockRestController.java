package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("/api/v1/stock/")
public class StockRestController {
    @Value(value = "${feign.token}")
    private String token;
    private final ServiceFeignClient serviceFeignClient;

    public StockRestController(ServiceFeignClient serviceFeignClient) {
        this.serviceFeignClient = serviceFeignClient;
    }

    @GetMapping("/financials/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public FinancialStatementDto financials(@PathVariable("symbol") String symbol) {
        return serviceFeignClient.getFinance(symbol, token);
    }

    @GetMapping("/metric/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    public MetricDto metric(@PathVariable("symbol") String symbol) {
        return serviceFeignClient.getMetric(symbol, token);
    }
}