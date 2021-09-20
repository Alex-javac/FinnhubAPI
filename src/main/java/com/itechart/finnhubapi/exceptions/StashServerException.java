package com.itechart.finnhubapi.exceptions;

public class StashServerException extends Exception {
    private final int status;
    private final String response;

    public StashServerException(int status, String response) {
        this.status = status;
        this.response = response;
    }
}
