package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDto {
    private double weekHigh;
    private Date weekHighDate;
    private double weekLow;
    private Date weekLowDate;
    private double weekPriceReturnDaily;
}
