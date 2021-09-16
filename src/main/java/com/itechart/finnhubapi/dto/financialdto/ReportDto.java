package com.itechart.finnhubapi.dto.financialdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    @JsonProperty("bs")
    List<ReportDetailsDto> bs;
    @JsonProperty("cf")
    List<ReportDetailsDto> cf;
    @JsonProperty("ic")
    List<ReportDetailsDto> ic;
}