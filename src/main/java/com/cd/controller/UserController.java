package com.cd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cd.dto.UserRequest;
import com.cd.dto.UserResponse;
import com.cd.model.UserModel;
import com.cd.service.UserService;

@CrossOrigin
@RestController
@RequestMapping(value="/api")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/login")
	public ResponseEntity<UserResponse> validateUser(@RequestBody UserRequest userRequest) {
		return ResponseEntity.ok(userService.validateUser(userRequest));
	}

	@PostMapping(value = "/create-user")
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
		return ResponseEntity.ok(userService.createUser(userRequest));
	}

	@PostMapping(value="/update-user")
	public ResponseEntity<UserResponse> updatePassword(@RequestBody UserRequest userRequest){
		return ResponseEntity.ok(userService.updatePassword(userRequest));
	}
}
