package com.itechart.finnhubapi.exceptions;

public class CheckValueException extends RuntimeException {
    public CheckValueException(String str) {
        super(String.format("%s incorrect", str));
    }
}
