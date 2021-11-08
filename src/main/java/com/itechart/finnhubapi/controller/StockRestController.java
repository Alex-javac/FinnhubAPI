package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/stock/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockRestController {

    private final StockService stockService;

    @GetMapping("/financials/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<FinancialStatementDto> financials(@PathVariable("symbol") String symbol) {
        return stockService.getFinancialResponseEntity(symbol);
    }

    @GetMapping("/metric/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MetricDto> metric(@PathVariable("symbol") String symbol) {
        return stockService.getMetricResponseEntity(symbol);
    }
}