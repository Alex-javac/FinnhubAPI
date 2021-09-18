package com.itechart.finnhubapi.mapper;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.model.CompanyEntity;
import com.itechart.finnhubapi.model.QuoteEntity;
import com.itechart.finnhubapi.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MapStructMapper {
    UserDto userToUserDto(UserEntity user);
    UserEntity userDtoToUserEntity(UserDto userDto);

    QuoteDto quoteToQuoteDto(QuoteEntity quote);
    QuoteEntity quoteDtoToQuoteEntity(QuoteDto quoteDto);

    CompanyDto companyToCompanyDto (CompanyEntity company);
    CompanyEntity companyDtoToCompanyEntity(CompanyDto companyDto);
}
