package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDetailsDto {
    @JsonProperty("52WeekHigh")
    private String weekHigh;
    @JsonProperty("52WeekHighDate")
    private String weekHighDate;
    @JsonProperty("52WeekLow")
    private String weekLow;
    @JsonProperty("52WeekLowDate")
    private String weekLowDate;
    @JsonProperty("52WeekPriceReturnDaily")
    private String weekPriceReturnDaily;
}
