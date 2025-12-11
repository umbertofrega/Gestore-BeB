package com.piattaforme.gestorebeb.model.exceptions.conflict;

public class ReservationDatesMismatchException extends ConflictException {
    public ReservationDatesMismatchException(String message) {
        super(message);
    }
}
