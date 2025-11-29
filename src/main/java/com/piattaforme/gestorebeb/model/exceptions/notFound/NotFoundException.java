package com.piattaforme.gestorebeb.model.exceptions.notFound;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
