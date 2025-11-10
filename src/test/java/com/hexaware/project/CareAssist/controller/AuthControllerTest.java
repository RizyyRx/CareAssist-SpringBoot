package com.hexaware.project.CareAssist.controller;

import com.hexaware.project.CareAssist.dto.LoginDTO;
import com.hexaware.project.CareAssist.dto.RegisterDTO;
import com.hexaware.project.CareAssist.dto.JwtAuthResponse;
import com.hexaware.project.CareAssist.service.AuthService;
import com.hexaware.project.CareAssist.service.PasswordResetService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})

class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private PasswordResetService passwordResetService;

    @MockitoBean
    private com.hexaware.project.CareAssist.jwt.JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        RegisterDTO dto = new RegisterDTO("rizwan", "rizwan@gmail.com", "pass123", "ROLE_PATIENT");

        when(authService.register(any(RegisterDTO.class))).thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }


    @Test
    void testRegister_DuplicateUser() throws Exception {
        RegisterDTO dto = new RegisterDTO("rizwan", "rizwan@gmail.com", "pass123","ROLE_PATIENT");
        doThrow(new RuntimeException("Username already exists"))
                .when(authService).register(any(RegisterDTO.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO login = new LoginDTO("rizwan", "pass123");
        when(authService.login(any(LoginDTO.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mock-jwt-token"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginDTO login = new LoginDTO("rizwan", "wrongpass");
        doThrow(new RuntimeException("Invalid credentials"))
                .when(authService).login(any(LoginDTO.class));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials"));
    }
}
