package com.sav.piecederechange.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);  // Appelle le constructeur de RuntimeException avec le message
    }
}