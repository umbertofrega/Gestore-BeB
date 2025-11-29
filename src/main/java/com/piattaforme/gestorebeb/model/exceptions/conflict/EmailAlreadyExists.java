package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
}
