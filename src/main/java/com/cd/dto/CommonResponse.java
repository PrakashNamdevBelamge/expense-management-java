package com.cd.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {

	private String statusCode;
	private Object message;
	private LocalDateTime timeStamp;
	
}
