package com.itechart.finnhubapi.config;

public class StashClientException extends Exception {
    private final int status;
    private final String response;

    public StashClientException(int status, String response) {
        this.status = status;
        this.response = response;
    }
}
