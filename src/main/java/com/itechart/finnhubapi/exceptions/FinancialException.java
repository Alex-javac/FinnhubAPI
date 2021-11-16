package com.itechart.finnhubapi.exceptions;

public class FinancialException extends RuntimeException{
    public FinancialException(String str){
        super(String.format("your subscription: %s "  +
                "to obtain data on financial statements of companies, HIGH subscription is required " +
                "to change your subscription follow the link: " +
                "http://localhost:8080/api/v1/subscription/payment", str));
    }
}
