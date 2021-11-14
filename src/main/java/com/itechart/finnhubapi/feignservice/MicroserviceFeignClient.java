package com.itechart.finnhubapi.feignservice;

import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.QuoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "microservice", url = "http://localhost:8181")
public interface MicroserviceFeignClient {
    @GetMapping("/microservice/getQuote/{symbol}")
    List<QuoteDto> getQuote(@PathVariable String symbol);

    @PostMapping("/microservice/saveQuote")
    void saveQuote();

    @PostMapping("/settings/deleteCompany/{symbol}")
    void deleteCompany(@PathVariable String symbol);

    @PostMapping("/settings/saveCompany")
    void saveCompany(@RequestBody CompanyDtoRequest companyDtoRequest);
}