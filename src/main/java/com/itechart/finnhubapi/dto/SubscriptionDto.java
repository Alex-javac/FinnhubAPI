package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionDto {
    @JsonProperty("status")
    private String status;
    @JsonProperty("startTime")
    private LocalDateTime startTime;
    @JsonProperty("finishTime")
    private LocalDateTime finishTime;
    @JsonProperty("type")
    private SubscriptionTypeDto type;
}
