package com.itechart.finnhubapi.dto.financialdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialStatementDataDto {
    @JsonProperty("accessNumber")
    private String accessNumber;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("cik")
    private String cik;
    @JsonProperty("year")
    private String year;
    @JsonProperty("quarter")
    private String quarter;
    @JsonProperty("form")
    private String form;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("filedDate")
    private String filedDate;
    @JsonProperty("acceptedDate")
    private String acceptedDate;
    @JsonProperty("report")
    private ReportDto report;
}