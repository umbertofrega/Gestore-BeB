package com.piattaforme.gestorebeb.model.exeptions;

public class RoomAlredyExistsException extends RuntimeException {
    public RoomAlredyExistsException(String message) {
        super(message);
    }
}
