package com.cd.model;

import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserModel {

	@GeneratedValue
	@Id
	private UUID id;
	@NotBlank(message = "UserName is required")
	private String userName;
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please enter email in proper format")
	private String email;
	@NotBlank(message = "Password is required")
	@Size(max = 15, min = 6, message = "Password size should be between 6 to 15 characters")
	private String password;
}
