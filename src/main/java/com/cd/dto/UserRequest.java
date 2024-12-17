package com.cd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

	private String id;
	
	@NotBlank(message = "UserName is required")
	private String userName;
	
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(max = 15, min = 6, message = "Password size should be between 6 to 15 characters")
	private String password;
}
