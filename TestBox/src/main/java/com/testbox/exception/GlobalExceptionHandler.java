package com.testbox.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================================
    // Validation Exceptions
    // ==========================================================

    // Handle Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle invalid path variable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {

        return new ResponseEntity<>(
                "Invalid input. Please provide a valid numeric ID.",
                HttpStatus.BAD_REQUEST);
    }

    // Handle invalid JSON request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {

        return new ResponseEntity<>(
                "Invalid request. Please check the request body and data types.",
                HttpStatus.BAD_REQUEST);
    }

    // Handle business validation exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // ==========================================================
    // Security Exceptions
    // ==========================================================

    // Handle invalid login credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(
            InvalidCredentialsException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED);
    }

    // Handle unauthorized access (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(
            AccessDeniedException ex) {

        return new ResponseEntity<>(
                "You are not authorized to access this resource.",
                HttpStatus.FORBIDDEN);
    }

    // Handle authentication failure (401)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(
            AuthenticationException ex) {

        return new ResponseEntity<>(
                "Authentication failed.",
                HttpStatus.UNAUTHORIZED);
    }

    // ==========================================================
    // User Exceptions
    // ==========================================================

    // Handle duplicate email
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.CONFLICT);
    }

    // Handle user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // Exam Exceptions
    // ==========================================================

    // Handle exam not found
    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<String> handleExamNotFoundException(
            ExamNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // Handle duplicate exam attempt
    @ExceptionHandler(ExamAttemptAlreadyExistsException.class)
    public ResponseEntity<String> handleExamAttemptAlreadyExistsException(
            ExamAttemptAlreadyExistsException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.CONFLICT);
    }

    // Handle exam attempt not found
    @ExceptionHandler(ExamAttemptNotFoundException.class)
    public ResponseEntity<String> handleExamAttemptNotFoundException(
            ExamAttemptNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // Question Exceptions
    // ==========================================================

    // Handle question not found
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handleQuestionNotFoundException(
            QuestionNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // Student Answer Exceptions
    // ==========================================================

    // Handle student answer not found
    @ExceptionHandler(StudentAnswerNotFoundException.class)
    public ResponseEntity<String> handleStudentAnswerNotFoundException(
            StudentAnswerNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // Result Exceptions
    // ==========================================================

    // Handle result not found
    @ExceptionHandler(ResultNotFoundException.class)
    public ResponseEntity<String> handleResultNotFoundException(
            ResultNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // Generic Exception
    // ==========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {

        ex.printStackTrace();

        return new ResponseEntity<>(
                "Something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}