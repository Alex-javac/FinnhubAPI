package com.itechart.finnhubapi.exceptions;

public class PayPalException extends RuntimeException {
    public PayPalException(String message) {
        super(message);
    }
}