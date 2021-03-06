package com.itechart.finnhubapi.dto.financialdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialStatementDto {
    @JsonProperty("cik")
    private String cik;
    @JsonProperty("data")
    private List<FinancialStatementDataDto> financialStatementDataDtoList;
    @JsonProperty("symbol")
    private String symbol;
}