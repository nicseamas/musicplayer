package com.musicplayer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (msg1, msg2) -> msg1 
                ));

        
       return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed","Input validation failed",request.getRequestURI(),errors);
    }

    
  @ExceptionHandler(SongNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleSongNotFound(SongNotFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI());
}

    
  
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex,HttpServletRequest request) {
        return buildErrorResponse( HttpStatus.BAD_REQUEST,"Bad Request",ex.getMessage(),request.getRequestURI());
    }


    
     @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex,HttpServletRequest request) {ex.printStackTrace();
return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error","An unexpected error occurred",request.getRequestURI());
}

   
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
        HttpStatus status, String error, String message, String path) {

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", error);
    body.put("message", message);
    body.put("path", path);

    return new ResponseEntity<>(body, status);
}

@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<Map<String, Object>> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request) {

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", "Invalid ID format: " + ex.getValue());
    body.put("path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
public ResponseEntity<Map<String, Object>> handleMethodNotAllowed(
        HttpRequestMethodNotSupportedException ex,
        HttpServletRequest request) {

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
    body.put("error", "Method Not Allowed");
    body.put("message", ex.getMessage());
    body.put("path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
}


private ResponseEntity<Map<String, Object>> buildErrorResponse(
HttpStatus status,String error,String message,String path,Map<String, String> validationErrors) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("errors", validationErrors);
        body.put("path", path);

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex,HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST,"Malformed JSON Request","Invalid JSON format",request.getRequestURI());
    }
}
