package com.hexaware.project.CareAssist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByUserId(int userId);
	
	void deleteByUserId(int userId);
}
