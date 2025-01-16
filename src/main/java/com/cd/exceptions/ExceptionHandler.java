package com.cd.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cd.commons.Constants;
import com.cd.dto.CommonResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse> handleIdMismatch(CustomException ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400, ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse> handleFieldError(MethodArgumentNotValidException ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400,
				ex.getFieldError().getDefaultMessage(), LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse> handleAllExceptions(Exception ex) {
		CommonResponse commonResponse = new CommonResponse(Constants.STATUS_CODE_400, ex.getMessage(),
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
	}
}
