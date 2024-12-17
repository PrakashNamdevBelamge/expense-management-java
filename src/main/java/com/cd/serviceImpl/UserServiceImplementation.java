package com.cd.serviceImpl;

import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cd.dto.UserRequest;
import com.cd.dto.UserResponse;
import com.cd.exceptions.CustomException;
import com.cd.model.UserModel;
import com.cd.repository.UserRepository;
import com.cd.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImplementation(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserResponse createUser(UserRequest userRequest) {
		if (this.userRepository.existsByUserName(userRequest.getUserName())) {
			throw new CustomException("UserName already exists.");
		}
		if (this.userRepository.existsByEmail(userRequest.getEmail())) {
			throw new CustomException("Email already exists.");
		}
		UserModel userModel = this.modelMapper.map(userRequest, UserModel.class);
		byte[] password = Base64.getDecoder().decode(userModel.getPassword());
		userModel.setPassword(new String(password));
		userModel = this.userRepository.save(userModel);
		UserResponse userResponse = this.modelMapper.map(userModel, UserResponse.class);
		return userResponse;
	}

	@Override
	public UserResponse validateUser(UserRequest userRequest) {
		byte[] password = Base64.getDecoder().decode(userRequest.getPassword());
		userRequest.setPassword(new String(password));
		UserModel userModel = this.userRepository
				.findByUserNameAndPassword(userRequest.getUserName(), userRequest.getPassword())
				.orElseThrow(() -> new CustomException("Invalid user credentials"));
		UserResponse userResponse = modelMapper.map(userModel, UserResponse.class);
		return userResponse;
	}

	@Override
	public UserResponse updatePassword(UserRequest userRequest) {
		UserModel userModel = this.userRepository.findByEmail(userRequest.getEmail())
				.orElseThrow(() -> new CustomException("User email not exists"));
		userModel.setPassword(userRequest.getPassword());
		this.userRepository.save(userModel);
		UserResponse userResponse = modelMapper.map(userModel, UserResponse.class);
		return userResponse;
	}
}
