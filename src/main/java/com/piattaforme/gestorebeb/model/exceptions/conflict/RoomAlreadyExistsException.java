package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class RoomAlreadyExistsException extends ConflictException {
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}
