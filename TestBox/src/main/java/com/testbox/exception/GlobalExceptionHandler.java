package com.testbox.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handle validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex)
	{
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
	});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
}
	
	//Handle duplicate email
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailAlreadyExistsExceptions(
			EmailAlreadyExistsException ex)
	{
		
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
		
	}
	
	//handle user not found
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex)
	{
		return new ResponseEntity<>(ex.getMessage(),
				HttpStatus.NOT_FOUND);
				
	}
	
	//handle invalid credentials
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentialsException(
			InvalidCredentialsException ex)
	{
		System.out.println("Message from Exception = [" + ex.getMessage() + "]");
		
		return new ResponseEntity<>(ex.getMessage(),
				HttpStatus.UNAUTHORIZED);
	}
	
	//handle all the exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex)
	{
		return new ResponseEntity<>(
				ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
				
	}
	
	// Handle invalid path variable type
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
	        MethodArgumentTypeMismatchException ex) {

	    return new ResponseEntity<>(
	            "Invalid input. Please provide a valid numeric ID.",
	            HttpStatus.BAD_REQUEST);
	}

}
