package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialStatementDataDto {

    private String accessNumber;
    private String symbol;
    private String cik;
    private String year;
    private String quarter;
    private String form;
    private String startDate;
    private String endDate;
    private String filedDate;
    private String acceptedDate;
    @JsonProperty("report")
    private ReportDto report;
}
