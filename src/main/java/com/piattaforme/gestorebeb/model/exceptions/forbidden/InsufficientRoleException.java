package com.piattaforme.gestorebeb.model.exceptions.forbidden;

public class InsufficientRoleException extends ForbiddenException {
    public InsufficientRoleException(String message) {
        super(message);
    }
}
