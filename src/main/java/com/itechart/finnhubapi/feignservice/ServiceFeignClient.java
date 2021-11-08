package com.itechart.finnhubapi.feignservice;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.dto.financialdto.FinancialStatementDto;
import com.itechart.finnhubapi.dto.metricdto.MetricDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "finnhub", url = "https://finnhub.io/api/v1")
public interface ServiceFeignClient {
    @GetMapping("/stock/symbol?exchange=US&token={token}")
    List<CompanyDto> getCompany(@PathVariable String token);

    @GetMapping("/stock/metric?symbol={symbol}&metric=all&token={token}")
    MetricDto getMetric(@PathVariable(name = "symbol") String symbol,
                        @PathVariable(name = "token") String token);

    @GetMapping("/stock/financials-reported?symbol={symbol}&token={token}")
    FinancialStatementDto getFinance(@PathVariable(name = "symbol") String symbol,
                                     @PathVariable(name = "token") String token);

    @GetMapping("/quote?symbol={symbol}&token={token}")
    QuoteDto getQuote(@PathVariable(name = "symbol") String symbol,
                      @PathVariable(name = "token") String token);
}