package com.hexaware.project.CareAssist.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;

public class GetAllUserDTO {

	@NotBlank(message = "userId is required")
	private int userId;
	
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	@NotBlank(message = "createdAt is required")
	private LocalDateTime createdAt;
	
	@NotBlank(message = "Role is required")
	private String role;

	public String getRole() {
	    return role;
	}

	public void setRole(String role) {
	    this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

