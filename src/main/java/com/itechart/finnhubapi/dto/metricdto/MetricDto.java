package com.itechart.finnhubapi.dto.metricdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDto {
    @JsonProperty("metric")
   private MetricDetailsDto metricDetails;
}
