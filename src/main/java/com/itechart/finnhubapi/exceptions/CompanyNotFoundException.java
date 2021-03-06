package com.itechart.finnhubapi.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String symbol){
        super(String.format("company named %s was not found", symbol));
    }
}
