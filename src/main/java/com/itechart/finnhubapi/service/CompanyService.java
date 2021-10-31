package com.itechart.finnhubapi.service;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.model.CompanyEntity;

import java.util.List;

public interface CompanyService {

    CompanyEntity getEntityBySymbol(String symbol);

    CompanyDto getBySymbol(String symbol);

    boolean save(List<CompanyDto> companyDtos);

    List<CompanyDto> getAllCompanyFromFeign();

    List<CompanyEntity> findAll();

    boolean saveQuote(List<CompanyEntity> company);

    boolean deleteCompany(String symbol);
}
