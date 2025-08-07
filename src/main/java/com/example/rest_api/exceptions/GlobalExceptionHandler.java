package com.example.rest_api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception,
                                                                   HttpServletRequest request) {
        String defaultMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(defaultMessage)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                               HttpServletRequest request) {
        List<String> validationError = exception.getBindingResult().getFieldErrors().stream()
                .map(ve -> ve.getField() + " : " + ve.getDefaultMessage()).toList();
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .errors(validationError)
                .build();
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception,
                                                                        HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(NOT_FOUND.value())
                .error(NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(NOT_FOUND).body(response);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                               HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(CONFLICT.value())
                .error(CONFLICT.getReasonPhrase())
                .message("The operation has failed, the request violate an persistence integrity constraint")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(CONFLICT).body(response);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                               HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Invalid format in request body. Please check your JSON syntax")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                                                   HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Invalid value for path variable '" + exception.getName() + "'")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(INTERNAL_SERVER_ERROR.value())
                .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(HttpServletRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(INTERNAL_SERVER_ERROR.value())
                .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred with entity values")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }
}
