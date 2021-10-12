package com.itechart.finnhubapi.dto.financialdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    @JsonProperty("bs")
    private List<ReportDetailsDto> bs;
    @JsonProperty("cf")
    private List<ReportDetailsDto> cf;
    @JsonProperty("ic")
    private List<ReportDetailsDto> ic;
}