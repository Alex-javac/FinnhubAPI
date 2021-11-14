package com.itechart.finnhubapi.exceptions;

public class SubscriptionPaidException extends RuntimeException{
    public SubscriptionPaidException(String message) {
        super("You already paid for the subscription: " + message);
    }
}