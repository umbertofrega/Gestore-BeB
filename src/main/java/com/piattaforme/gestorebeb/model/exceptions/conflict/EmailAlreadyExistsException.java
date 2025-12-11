package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
