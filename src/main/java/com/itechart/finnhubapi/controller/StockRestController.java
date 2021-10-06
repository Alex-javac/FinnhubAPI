package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import com.itechart.finnhubapi.feignservice.ServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    @Value(value = "${feign.token}")
    private String token;
    private final ServiceFeignClient serviceFeignClient;

    @GetMapping("/financials/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<FinancialStatementDto> financials(@PathVariable("symbol") String symbol) {
        FinancialStatementDto finance = serviceFeignClient.getFinance(symbol, token);
        return new ResponseEntity<>(finance, HttpStatus.OK);
    }

    @GetMapping("/metric/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MetricDto> metric(@PathVariable("symbol") String symbol) {
        MetricDto metric = serviceFeignClient.getMetric(symbol, token);
        return new ResponseEntity<>(metric, HttpStatus.OK);
    }
}