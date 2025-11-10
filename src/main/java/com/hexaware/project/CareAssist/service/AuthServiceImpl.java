package com.hexaware.project.CareAssist.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.entity.Role;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.jwt.JwtTokenProvider;
import com.hexaware.project.CareAssist.repository.RoleRepository;
import com.hexaware.project.CareAssist.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
    private JavaMailSender mailSender;

	private final AuthenticationManager authenticationManager ;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    
    
	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
    
    
    @Override
    public String login(LoginDTO loginDTO) {
    	try {
    		// Try to find user first
            User user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                    .orElseThrow(() -> new RuntimeException("Wrong credentials"));

            // Enforce case-sensitive username login, email remains case-insensitive
            boolean isEmailLogin = loginDTO.getUsernameOrEmail().equalsIgnoreCase(user.getEmail());
            if (!isEmailLogin && !loginDTO.getUsernameOrEmail().equals(user.getUsername())) {
                throw new RuntimeException("Wrong credentials");
            }

        	// calls CustomUserDetailsService for authentication
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            		loginDTO.getUsernameOrEmail(),
            		loginDTO.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            return token;
    	} catch (Exception e) {
            // Always return a unified message for any error
            throw new RuntimeException("Wrong credentials");
        }
    }

	@Override
	public String register(RegisterDTO registerDTO) {
		
	    if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
	        throw new RuntimeException("Username already exists.");
	    }

	    if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
	        throw new RuntimeException("Email already exists.");
	    }

		User user = new User();
	    user.setUsername(registerDTO.getUsername());
	    user.setEmail(registerDTO.getEmail());
	    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

	    String requestedRole = registerDTO.getRole().toUpperCase();

	    // Only allow specific roles from client
	    if (!requestedRole.equals("PATIENT") &&
	        !requestedRole.equals("HEALTHCARE_PROVIDER") &&
	        !requestedRole.equals("ADMIN") &&
	        !requestedRole.equals("INSURANCE_COMPANY")) {
	        throw new RuntimeException("Invalid role");
	    }
	    
	    Role role = roleRepository.findByName("ROLE_" + requestedRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));

	    // Put role obj into a Set and set it to user obj
	    user.setRoles(Set.of(role));
	
	    userRepository.save(user);
	    
	    return "User registered successfully with role: " + registerDTO.getRole();
		
	}
	
	public String getUsername(String usernameOrEmail) {
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new RuntimeException("User not found with username or email: " + usernameOrEmail));

	    String username = user.getUsername();

	    if (username != null && !username.isEmpty()) {
	        username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
	    }

	    return username;
	}
	
	public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
