package com.hexaware.project.CareAssist.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.entity.Role;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.exception.CustomAccessDeniedHandler;
import com.hexaware.project.CareAssist.jwt.JwtAuthenticationEntryPoint;
import com.hexaware.project.CareAssist.jwt.JwtAuthenticationFilter;
import com.hexaware.project.CareAssist.jwt.JwtTokenProvider;
import com.hexaware.project.CareAssist.repository.RoleRepository;
import com.hexaware.project.CareAssist.repository.UserRepository;
import com.hexaware.project.CareAssist.service.AuthService;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {



	private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    
    public SpringSecurityConfig(UserDetailsService userDetailsService,
			JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter,
			CustomAccessDeniedHandler accessDeniedHandler, JwtTokenProvider jwtTokenProvider,
			UserRepository userRepository, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
		super();
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
		this.accessDeniedHandler = accessDeniedHandler;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}
 
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors->{})
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.requestMatchers("/uploads/**").permitAll();
                    authorize.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll();
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authorize.requestMatchers("/api/insurance-company/get-all").authenticated();
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth -> oauth
                	    .successHandler((request, response, authentication) -> {
                	        var oAuth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                	        String email = oAuth2User.getAttribute("email");
                	        Object nameObj = oAuth2User.getAttribute("name");
                	        String username = nameObj == null ? "unknownUser" : ((String) nameObj).replaceAll("\\s+", "");

                	        String role = "PATIENT";

                	        User existingUser = userRepository.findByEmail(email).orElse(null);
                	        
                	        if (existingUser == null) {
                	            User newUser = new User();
                	            newUser.setUsername(username);
                	            newUser.setEmail(email);
                	            newUser.setPassword(passwordEncoder.encode(username)); // dummy password

                	            // Set role manually
                	            Role roleEntity = roleRepository.findByName("ROLE_PATIENT")
                	                    .orElseThrow(() -> new RuntimeException("Role not found"));

                	            Set<Role> roles = new HashSet<>();
                	            roles.add(roleEntity);
                	            newUser.setRoles(roles);
                	            userRepository.save(newUser);

                	            existingUser = newUser;
                	        }

                	        var auth = new UsernamePasswordAuthenticationToken(
                	        	existingUser.getUsername(),
                	            null,
                	            List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
                	        );

                	        String token = jwtTokenProvider.generateToken(auth);
                	        String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" + token;
                	        response.sendRedirect(redirectUrl);
                	        
                	    })
                	)

                .httpBasic(Customizer.withDefaults());
 
        http.exceptionHandling( exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
 
        return http.build();
    }
 
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
