package com.example.carcare.exception;

public class DeleteEntityException extends RuntimeException {

    public DeleteEntityException(String nameClass, Long id, Throwable cause) {
        super("Error al borrar la entidad " + nameClass + " con id " + id, cause);
    }
}