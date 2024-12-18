package com.cd.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	@Column(name = "user_name", length = 100, nullable = false)
	private String userName;
	@Column(name = "email", length = 100, nullable = false)
	private String email;
	@Column(name = "password", length = 100, nullable = false)
	private String password;
}
