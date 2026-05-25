package com.example.carcare.exception;

public class ErrorException extends RuntimeException {

    public <T> ErrorException(String message, Throwable cause) {
        super("Error genérico: " + message, cause);
    }
}