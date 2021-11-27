package com.itechart.finnhubapi.exceptions;

public class JwtAuthenticationException extends RuntimeException{
    public JwtAuthenticationException (String str){
        super(str);
    }
}
