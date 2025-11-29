package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class RoomOccupiedException extends ConflictException {
    public RoomOccupiedException(String message) {
        super(message);
    }
}
