package com.example.carcare.exception;

public class UpdateEntityException extends RuntimeException {

    public <T> UpdateEntityException(String nameClass, T entityClass, Throwable cause) {
        super("Error al modificar la entidad " + nameClass + " ( " + entityClass.toString() + " )", cause);
    }
}