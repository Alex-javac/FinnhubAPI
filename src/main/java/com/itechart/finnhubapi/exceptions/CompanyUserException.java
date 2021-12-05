package com.itechart.finnhubapi.exceptions;

public class CompanyUserException extends RuntimeException{
    public CompanyUserException(){
        super("the list of companies is empty");
    }
}
