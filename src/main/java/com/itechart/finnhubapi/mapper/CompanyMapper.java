package com.itechart.finnhubapi.mapper;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.model.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto companyToCompanyDto(CompanyEntity company);

    CompanyEntity companyDtoToCompanyEntity(CompanyDto companyDto);

    CompanyDtoRequest companyToCompanyDtoRequest(CompanyEntity company);
}