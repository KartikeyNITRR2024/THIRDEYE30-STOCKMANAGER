package com.thirdeye3.stockmanager.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.thirdeye3.stockmanager.dtos.Response;
import com.thirdeye3.stockmanager.exceptions.CSVException;
import com.thirdeye3.stockmanager.exceptions.InvalidMachineException;
import com.thirdeye3.stockmanager.exceptions.InvalidTimeException;
import com.thirdeye3.stockmanager.exceptions.PropertyFetchException;
import com.thirdeye3.stockmanager.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMachineException.class)
    public ResponseEntity<Response<Void>> handleInvalidMachine(InvalidMachineException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyFetchException.class)
    public ResponseEntity<Response<Void>> handlePropertyFetch(PropertyFetchException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(CSVException.class)
    public ResponseEntity<Response<Void>> handleCSVException(CSVException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.EXPECTATION_FAILED.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    
    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<Response<Void>> handleCSVException(InvalidTimeException ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    
    
    
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneric(Exception ex) {
        Response<Void> response = new Response<>(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error occurred: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
