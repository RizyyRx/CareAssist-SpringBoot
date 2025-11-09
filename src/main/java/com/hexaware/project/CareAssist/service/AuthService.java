package com.hexaware.project.CareAssist.service;

import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.entity.User;

public interface AuthService {

	String login(LoginDTO loginDTO);
	
	String register(RegisterDTO registerDTO);
	
	String getUsername(String usernameOrEmail);
}
