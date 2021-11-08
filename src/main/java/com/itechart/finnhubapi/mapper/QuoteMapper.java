package com.itechart.finnhubapi.mapper;

import com.itechart.finnhubapi.dto.QuoteDto;
import com.itechart.finnhubapi.model.QuoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuoteMapper {
    QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

    QuoteDto quoteToQuoteDto(QuoteEntity quote);

    QuoteEntity quoteDtoToQuoteEntity(QuoteDto quoteDto);
}