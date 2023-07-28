package com.db.dataplatform.techtest.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends Exception {

    EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public EntityNotFoundException(final String message) {
        super(message);
    }
}
