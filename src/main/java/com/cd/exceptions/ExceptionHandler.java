package com.cd.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cd.commons.Constants;
import com.cd.dto.CommonResponse;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleIdMismatch(CustomException ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400, ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleFieldError(MethodArgumentNotValidException ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400,
				ex.getFieldError().getDefaultMessage(), LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ResponseEntity<CommonResponse> handleAllExceptions(Exception ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400, ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}
}
