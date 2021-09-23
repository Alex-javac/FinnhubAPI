package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("description")
    private String description;
    @JsonProperty("displaySymbol")
    private String displaySymbol;
    @JsonProperty("figi")
    private String figi;
    @JsonProperty("mic")
    private String mic;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("type")
    private String type;
}
