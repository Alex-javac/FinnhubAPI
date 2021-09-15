package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialStatementDto {
    private String accessNumber;
    private String symbol;
    private String cik;
    private Date year;
    private String quarter;
    private String form;
    private Date startDate;
    private Date endDate;
    private Date filedDate;
    private Date acceptedDate;

    List<ReportDto> bs;
    List<ReportDto> cf;
    List<ReportDto> ic;
}