package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.model.CompanyEntity;

import java.util.List;

public interface CompanyService {

    List<QuoteDto> getQuote(String symbol);

    CompanyEntity getEntityBySymbol(String symbol);

    CompanyDto getBySymbol(String symbol);

    boolean save(List<CompanyDto> companyDtos);

    List<CompanyDto> getAllCompanyFromFeign();

    List<CompanyEntity> findAll();

    boolean saveQuote();

    boolean deleteCompany(String symbol);
}