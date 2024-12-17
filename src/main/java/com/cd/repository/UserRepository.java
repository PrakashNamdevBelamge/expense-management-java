package com.cd.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cd.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>{

	boolean existsByUserName(String userName);
	
	boolean existsByEmail(String email);
		
	Optional<UserModel> findByEmail(String email);

	Optional<UserModel> findByUserNameAndPassword(String userName, String password);
}
