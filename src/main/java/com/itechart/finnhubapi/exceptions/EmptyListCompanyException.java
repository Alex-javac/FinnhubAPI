package com.itechart.finnhubapi.exceptions;

public class EmptyListCompanyException extends RuntimeException{
    public EmptyListCompanyException(){
        super("list is empty. need add company");
    }
}
