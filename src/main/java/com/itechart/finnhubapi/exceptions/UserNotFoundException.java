package com.itechart.finnhubapi.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User with Id %d not found", id));
    }
    public UserNotFoundException(String name) {
        super(String.format("User with name %s doesn't exists", name));
    }
}