package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class EmailAlreadyExists extends ConflictException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
}
