package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/company", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyRestController {

    private final CompanyService companyService;

    @GetMapping(value = "/getAllCompanies")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> data = companyService.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/getOneCompany/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CompanyDto> getOneCompany(@PathVariable("symbol") String symbol) {
        CompanyDto companyDto = companyService.getBySymbol(symbol);
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @GetMapping(value = "/getQuote/{symbol}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<QuoteDto>> getQuote(@PathVariable("symbol") String symbol) {
        List<QuoteDto> quoteDto = companyService.getQuote(symbol);
        return new ResponseEntity<>(quoteDto, HttpStatus.OK);
    }

    @PostMapping(value = "/saveAllCompanies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> saveCompanyToDB() {
        List<CompanyDto> companyFromFeign = companyService.getAllCompanyFromFeign();
        return (companyService.save(companyFromFeign)) ?
                new ResponseEntity<>("successful save Companies", HttpStatus.OK) :
                new ResponseEntity<>("an error occurred during saving", HttpStatus.NOT_MODIFIED);

    }

    @PostMapping(value = "/saveQuotes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> saveQuoteToDB() {
        return (companyService.saveQuote()) ?
                new ResponseEntity<>("successful save Quotes", HttpStatus.OK) :
                new ResponseEntity<>("an error occurred during saving", HttpStatus.NOT_MODIFIED);
    }

    @PostMapping(value = "/deleteCompany")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCompany(@RequestBody CompanyDtoRequest companyDtoRequest) {
        return (companyService.deleteCompany(companyDtoRequest.getSymbol())) ?
                new ResponseEntity<>("successful delete", HttpStatus.OK) :
                new ResponseEntity<>("there was an error during deletion", HttpStatus.NOT_MODIFIED);

    }
}