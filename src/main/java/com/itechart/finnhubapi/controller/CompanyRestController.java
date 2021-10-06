package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.UserEntity;
import com.itechart.finnhubapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/company",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyRestController {

    private final CompanyService companyService;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CompanyEntity> company() {
        List<CompanyEntity> data = companyService.findAll();
        return data;
    }

    @PostMapping(value = "/saveAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public void saveCompanyToDB() {
        List<CompanyDto> companyFromFeign = companyService.getAllCompanyFromFeign();
        companyService.save(companyFromFeign);
    }

    @GetMapping(value = "/getOne/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public CompanyDto getOne(@PathVariable("symbol") String symbol) {
        return companyService.getBySymbol(symbol);
    }

    @PostMapping(value = "/saveQuotes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public void saveQuoteToDB() {
        List<CompanyEntity> company = companyService.findAll();
        companyService.saveQuote(company);

    }
}