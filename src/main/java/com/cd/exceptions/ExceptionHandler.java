package com.cd.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cd.dto.CommonResponse;

@RestControllerAdvice
public class ExceptionHandler { 
	


	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleIdMismatch(CustomException ex) {
		ex.printStackTrace();
		CommonResponse commonResponse = new CommonResponse("EMPLOYEE 400", ex.getMessage() , LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleFieldError(MethodArgumentNotValidException ex) {
		ex.printStackTrace();
		CommonResponse commonResponse = new CommonResponse("EMPLOYEE 400", ex.getFieldError().getDefaultMessage() , LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleAllExceptions(Exception ex) {
		ex.printStackTrace();
		CommonResponse commonResponse = new CommonResponse("EMPLOYEE 400", ex.getMessage() , LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}
}
