package com.example.carcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    // Captura fallos de @Valid en los DTOs y devuelve todos los campos inválidos en un único mensaje
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationArgumentsErrors(MethodArgumentNotValidException ex) {
        String mapAsString = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " = " + error.getDefaultMessage())
                .collect(Collectors.joining(", ", "{ ", " }"));

        return new ResponseEntity<>(Response.validationError(mapAsString), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CreateEntityException.class)
    public ResponseEntity<Response> handleCreateEntityException(CreateEntityException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UpdateEntityException.class)
    public ResponseEntity<Response> handleUpdateEntityException(UpdateEntityException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DeleteEntityException.class)
    public ResponseEntity<Response> handleDeleteEntityException(DeleteEntityException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response> handleNotResourceException(NoResourceFoundException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.NOT_FOUND.value(), "La ruta " + ex.getResourcePath() + " no existe"),
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Response> handleErrorException(ErrorException ex) {
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}