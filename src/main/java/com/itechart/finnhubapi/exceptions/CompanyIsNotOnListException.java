package com.itechart.finnhubapi.exceptions;

public class CompanyIsNotOnListException extends RuntimeException{
    public CompanyIsNotOnListException(String symbol){
        super(String.format("company with name %s is not in the list of the user", symbol));
    }
}