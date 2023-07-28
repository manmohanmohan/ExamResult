package com.db.dataplatform.techtest.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegrityException extends Exception {

    DataIntegrityException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public DataIntegrityException(final String message) {
        super(message);
    }
}

