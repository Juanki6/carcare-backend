package com.example.carcare.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String nameClass, Long id) {
        super("La entidad " + nameClass + " con id " + id + " no existe");
    }

    public NotFoundException(String message) {
        super(message);
    }
}