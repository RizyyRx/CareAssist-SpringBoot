package com.hexaware.project.CareAssist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hexaware.project.CareAssist.exception.CustomAccessDeniedHandler;
import com.hexaware.project.CareAssist.jwt.JwtAuthenticationEntryPoint;
import com.hexaware.project.CareAssist.jwt.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {



	private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;
    private CustomAccessDeniedHandler accessDeniedHandler;
    
    public SpringSecurityConfig(UserDetailsService userDetailsService,
			JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter, CustomAccessDeniedHandler accessDeniedHandler) {
		super();
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
		this.accessDeniedHandler = accessDeniedHandler;
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
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authorize.requestMatchers("/api/patient/update-profile").hasRole("PATIENT");
                    authorize.requestMatchers("/api/patient/select-plan").hasRole("PATIENT");
                    authorize.requestMatchers("/api/patient/invoice/mark-paid/**").hasRole("PATIENT");
                    authorize.requestMatchers("/api/patient/invoices").hasRole("PATIENT");
                    authorize.requestMatchers("/api/patient/submit-claim").hasRole("PATIENT");
                    authorize.requestMatchers("/api/provider/create-invoice").hasRole("HEALTHCARE_PROVIDER");
                    authorize.requestMatchers("/api/insurance-company/create").hasRole("INSURANCE_COMPANY");
                    authorize.requestMatchers("/api/insurance-company/claim/approve/**").hasRole("INSURANCE_COMPANY");
                    authorize.requestMatchers("/api/insurance-company/claim/process-payment").hasRole("INSURANCE_COMPANY");
                    authorize.requestMatchers("/api/insurance-company/get-claims/patient/**").hasAnyRole("INSURANCE_COMPANY","PATIENT");
                    authorize.requestMatchers("/api/insurance-company/get-all").authenticated();
                    authorize.requestMatchers("/**").hasRole("ADMIN");
                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());
 
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
