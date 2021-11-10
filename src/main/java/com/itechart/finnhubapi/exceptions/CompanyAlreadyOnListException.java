package com.itechart.finnhubapi.exceptions;

public class CompanyAlreadyOnListException extends RuntimeException{
    public CompanyAlreadyOnListException(String symbol){
        super(String.format("company named %s is already on the list", symbol));
    }
}
