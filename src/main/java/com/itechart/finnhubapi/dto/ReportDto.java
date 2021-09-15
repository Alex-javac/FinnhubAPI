package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    private String unit;
    private String label;
    private String value;
    private String concept;
}