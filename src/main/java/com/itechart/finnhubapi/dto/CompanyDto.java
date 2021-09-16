package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.finnhubapi.model.CompanyEntity;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {
    private String currency;
    private String description;
    private String displaySymbol;
    private String figi;
    private String mic;
    private String symbol;
    private String type;

    public CompanyEntity toCompanyEntity() {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCurrency(currency);
        companyEntity.setDescription(description);
        companyEntity.setDisplaySymbol(displaySymbol);
        companyEntity.setFigi(figi);
        companyEntity.setMic(mic);
        companyEntity.setSymbol(symbol);
        companyEntity.setType(type);
        return companyEntity;
    }

    public static CompanyDto fromCompanyEntity(CompanyEntity company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCurrency(company.getCurrency());
        companyDto.setDescription(company.getDescription());
        companyDto.setDisplaySymbol(company.getDisplaySymbol());
        companyDto.setFigi(company.getFigi());
        companyDto.setMic(company.getMic());
        companyDto.setSymbol(company.getSymbol());
        companyDto.setType(company.getType());
        return companyDto;
    }
}
