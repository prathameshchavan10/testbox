package com.testbox.exception;

public class ResultNotFoundException extends RuntimeException {

    public ResultNotFoundException(String message) {
        super(message);
    }
}