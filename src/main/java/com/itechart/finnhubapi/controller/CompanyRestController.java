package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/company", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyRestController {

    private final CompanyService companyService;

    @GetMapping(value = "/getAllCompanies")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> data = companyService.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneCompany/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CompanyDto> getOneCompany(@PathVariable("symbol") String symbol) {
        CompanyDto companyDto = companyService.getBySymbol(symbol);
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @PostMapping(value = "/saveAllCompanies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity saveCompanyToDB() {
        List<CompanyDto> companyFromFeign = companyService.getAllCompanyFromFeign();
        if (companyService.save(companyFromFeign)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/saveQuotes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity saveQuoteToDB() {
        List<CompanyEntity> company = companyService.findAll();
        if (companyService.saveQuote(company)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/deleteCompany/{symbol}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCompany(@PathVariable("symbol") String symbol) {
        if (!companyService.deleteCompany(symbol)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }
}