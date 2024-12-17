package com.cd.service;

import com.cd.dto.UserRequest;
import com.cd.dto.UserResponse;

public interface UserService {

	public UserResponse createUser(UserRequest userRequest);
	public UserResponse validateUser(UserRequest userRequest);
	public UserResponse updatePassword(UserRequest userRequestDTO);

}
