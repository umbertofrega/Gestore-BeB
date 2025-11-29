package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class RoomMismatchException extends RuntimeException {
    public RoomMismatchException(String message) {
        super(message);
    }
}
