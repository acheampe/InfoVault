package com.infovault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        logger.error("User already exists exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("User already exists", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        logger.error("Invalid input exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Invalid input", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("User not found exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("User not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Authentication failed", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CognitoRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleCognitoRegistrationException(CognitoRegistrationException ex) {
        logger.error("Cognito registration exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Cognito registration failed", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // You can add a catch-all handler for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Internal server error", "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
