package com.itechart.finnhubapi.exceptions;

public class SubscriptionTypeException extends RuntimeException{
    public SubscriptionTypeException(){
       super("SubscriptionType not found");
    }
}
