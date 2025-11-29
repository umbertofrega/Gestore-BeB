package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class RoomAlreadyExistsException extends RuntimeException {
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}
