package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.model.entity.CompanyEntity;

import java.util.List;

public interface CompanyService {

    List<QuoteDto> getQuote(String symbol, Long userId);

    CompanyEntity getEntityById(Long id);

    CompanyDto getBySymbol(String symbol);

    boolean save(List<CompanyDto> companyDtos);

    List<CompanyDto> getAllCompanyFromFeign();

    List<CompanyDto> findAll();

    boolean saveQuote();

    boolean deleteCompany(Long id);
}