package com.piattaforme.gestorebeb.model.exceptions;

public class UsedEmailException extends RuntimeException {
    public UsedEmailException(String message) {
        super(message);
    }
}
