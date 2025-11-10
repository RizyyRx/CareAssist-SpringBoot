package com.hexaware.project.CareAssist.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((FieldError error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();

        if (message != null) {
            if (message.contains("Duplicate entry")) {
                if (message.contains("username")) {
                    return ResponseEntity.badRequest().body("Username already exists");
                } else if (message.contains("email")) {
                    return ResponseEntity.badRequest().body("Email already exists");
                } else {
                    return ResponseEntity.badRequest().body("Duplicate entry — data already exists");
                }
            } else if (message.contains("Data too long for column")) {
                if (message.contains("username")) {
                    return ResponseEntity.badRequest().body("Username is too long — maximum 20 characters allowed");
                } else if (message.contains("email")) {
                    return ResponseEntity.badRequest().body("Email is too long — please shorten it");
                } else {
                    return ResponseEntity.badRequest().body("Input data too long for one of the fields");
                }
            } else if (message.contains("Out of range value for column")) {
                return ResponseEntity.badRequest().body("One of the numeric fields has a value that is too large");
            }
        }

        // Default fallback
        return ResponseEntity.badRequest().body("Invalid or duplicate data");
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }
    
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleMissingPathVariable(MissingPathVariableException ex) {
        String msg = "Missing path variable: " + ex.getVariableName();
        return ResponseEntity.badRequest().body(msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        String msg = "Missing request parameter: " + ex.getParameterName();
        return ResponseEntity.badRequest().body(msg);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        String msg = "Invalid input format. Please check the provided data.";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("java.lang.Integer")) {
                msg = "Invalid number format.";
            } else if (ex.getMessage().contains("LocalDate")) {
                msg = "Invalid date format. Please use yyyy-MM-dd.";
            }
        }

        return ResponseEntity.badRequest().body(msg);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // or use a logger
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage());
    }
}
