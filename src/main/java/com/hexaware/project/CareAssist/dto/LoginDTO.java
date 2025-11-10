package com.hexaware.project.CareAssist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {
	
	@NotBlank(message = "Username or email is required")
	@Size(min = 3, max = 30, message = "Username or Email must be between 3 and 30 characters")
	private String usernameOrEmail;
	
	@NotBlank(message = "password is required")
	@Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
	private String password;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}
	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public LoginDTO(
			@NotBlank(message = "Username or email is required") @Size(min = 3, max = 30, message = "Username or Email must be between 3 and 30 characters") String usernameOrEmail,
			@NotBlank(message = "password is required") @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters") String password) {
		super();
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
	}

}
