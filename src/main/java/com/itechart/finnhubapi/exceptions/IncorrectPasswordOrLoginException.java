package com.itechart.finnhubapi.exceptions;

public class IncorrectPasswordOrLoginException extends RuntimeException{
    public IncorrectPasswordOrLoginException(){
        super("Incorrect Password or Login");
    }
}
