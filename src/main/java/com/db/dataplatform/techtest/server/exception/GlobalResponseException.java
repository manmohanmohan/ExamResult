package com.db.dataplatform.techtest.server.exception;

import com.db.dataplatform.techtest.server.web.ApiError;
import com.db.dataplatform.techtest.server.web.ResponseEntityBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalResponseException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            EntityNotFoundException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Resource Not Found",
                details);

        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<Object> handleConstraintException(
            EntityNotFoundException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Resource exist in db",
                details);

        return ResponseEntityBuilder.build(err);
    }
}