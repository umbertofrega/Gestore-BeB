package com.piattaforme.gestorebeb.model.exceptions.notFound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
