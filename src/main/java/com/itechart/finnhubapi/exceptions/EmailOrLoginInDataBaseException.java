package com.itechart.finnhubapi.exceptions;

public class EmailOrLoginInDataBaseException extends RuntimeException{
    public EmailOrLoginInDataBaseException (String email, String login){
       super(String.format("user with login <%s> or email <%s> is already in database", login, email));
    }
}
