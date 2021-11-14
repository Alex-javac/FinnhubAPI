package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteDto {
    @JsonProperty("c")
    private double c;
    @JsonProperty("d")
    private double d;
    @JsonProperty("dp")
    private double dp;
    @JsonProperty("h")
    private double h;
    @JsonProperty("l")
    private double l;
    @JsonProperty("o")
    private double o;
    @JsonProperty("pc")
    private double pc;
    @JsonProperty("t")
    private double t;
    @JsonProperty("date")
    private LocalDateTime date;
}