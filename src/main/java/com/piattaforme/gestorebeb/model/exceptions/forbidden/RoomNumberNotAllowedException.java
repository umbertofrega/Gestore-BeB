package com.piattaforme.gestorebeb.model.exceptions.forbidden;

public class RoomNumberNotAllowedException extends ForbiddenException {
    public RoomNumberNotAllowedException(String message) {
        super(message);
    }
}
