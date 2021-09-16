package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialStatementDto {
    private String cik;
    @JsonProperty("data")
  private List<FinancialStatementDataDto> financialStatementDataDtoList;
    private String symbol;
}