package com.sav.categoriesService.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    // Constructeur sans message
    public ResourceNotFoundException() {
        super();
    }

    // Constructeur avec message d'erreur
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec message et cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
