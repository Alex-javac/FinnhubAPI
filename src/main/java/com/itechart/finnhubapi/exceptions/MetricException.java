package com.itechart.finnhubapi.exceptions;

public class MetricException extends RuntimeException {
    public MetricException(String str) {
        super(String.format("your subscription: %s " +
                "Inventory data for 52 weeks requires MEDIUM or HIGH subscription " +
                "to change your subscription follow the link: " +
                "http://localhost:8080/api/v1/subscription/payment", str));
    }
}