package com.example.student;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HashMap<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		
		HashMap<String, String> errorMessages = new HashMap<>();
		
		
		for(FieldError error: ex.getBindingResult().getFieldErrors()) {
			errorMessages.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
	}
	
	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
}
