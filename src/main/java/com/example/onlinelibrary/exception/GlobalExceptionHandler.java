package com.example.onlinelibrary.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(ResourceNotFoundException.class)
 public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
     return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
 }

 @ExceptionHandler(InvalidDataException.class)
 public ResponseEntity<String> handleInvalidData(InvalidDataException ex, WebRequest request) {
     return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
 }

 @ExceptionHandler(DataIntegrityException.class)
 public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityException ex, WebRequest request) {
     return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
 }

 @ExceptionHandler(Exception.class)
 public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
     return new ResponseEntity<>("An internal error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
 }
}