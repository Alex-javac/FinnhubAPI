package com.itechart.finnhubapi.exceptions;

public class CompanyUserOverflowException extends RuntimeException{
    public CompanyUserOverflowException(){
        super("The list of companies is overflowing. Upgrade to a higher subscription or remove the company from the list");
    }
}
