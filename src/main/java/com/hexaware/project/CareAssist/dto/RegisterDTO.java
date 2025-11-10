package com.hexaware.project.CareAssist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
	@NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(
    		regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    		message = "Invalid email format — only letters, numbers, and . _ % + - are allowed"
    		)
    @Size(max = 30, message = "Email must be below 30 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role; // e.g., "PATIENT", "HEALTHCARE_PROVIDER", "INSURANCE_COMPANY"
    
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
    public RegisterDTO(
			@NotBlank(message = "Username is required") @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") String username,
			@NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format — only letters, numbers, and . _ % + - are allowed") @Size(max = 30, message = "Email must be below 30 characters") String email,
			@NotBlank(message = "Password is required") @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters") String password,
			@NotBlank(message = "Role is required") String role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
