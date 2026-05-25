package com.example.carcare.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {

    private int code;
    private String message;

    private Response(int errorCode, String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    public static Response validationError(String error) {
        return new Response(HttpStatus.BAD_REQUEST.value(), error);
    }

    public static Response generalError(int code, String mensaje) {
        return new Response(code, mensaje);
    }
}