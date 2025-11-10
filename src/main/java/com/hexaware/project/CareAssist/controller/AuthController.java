package com.hexaware.project.CareAssist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.project.CareAssist.dto.JwtAuthResponse;
import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.service.AuthService;
import com.hexaware.project.CareAssist.service.PasswordResetService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
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
    

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        passwordResetService.generateResetToken(email);
        return ResponseEntity.ok("Reset link sent to your email if account exists.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || token.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Token and new password are required");
        }

        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
    
}
