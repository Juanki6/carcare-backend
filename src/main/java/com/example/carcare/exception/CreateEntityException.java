package com.example.carcare.exception;

public class CreateEntityException extends RuntimeException {

    public CreateEntityException(String message) {
        super(message);
    }

    public <T> CreateEntityException(String nameClass, T entityClass, Throwable cause) {
        super("Error al crear la entidad " + nameClass + " ( " + entityClass.toString() + " )", cause);
    }
}