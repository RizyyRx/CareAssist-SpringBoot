package com.hexaware.project.CareAssist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.project.CareAssist.dto.JwtAuthResponse;
import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    
    public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

    // Login user
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginDTO loginDto){
        String token = authService.login(loginDto);
 
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
 
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
    
    // Register user with role
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO){
    	String message = authService.register(registerDTO);
    	return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    
    @GetMapping("/get-username/{usernameOrEmail}")
    public ResponseEntity<String> getUsername(@PathVariable String usernameOrEmail) {
        String username = authService.getUsername(usernameOrEmail);
        return ResponseEntity.ok(username);
    }
    
}
