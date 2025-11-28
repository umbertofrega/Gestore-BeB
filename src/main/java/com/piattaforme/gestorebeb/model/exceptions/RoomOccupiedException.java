package com.piattaforme.gestorebeb.model.exceptions;

public class RoomOccupiedException extends RuntimeException {
    public RoomOccupiedException(String message) {
        super(message);
    }
}
