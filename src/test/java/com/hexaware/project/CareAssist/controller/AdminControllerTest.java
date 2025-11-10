package com.hexaware.project.CareAssist.controller;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllUserDTO;
import com.hexaware.project.CareAssist.service.AdminService;
import com.hexaware.project.CareAssist.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void testGetAllUsers_Success() throws Exception {
        GetAllUserDTO user1 = new GetAllUserDTO();
        user1.setUserId(1);
        user1.setUsername("Rizwan");
        user1.setEmail("rizwan@example.com");
        user1.setPassword("pass123");
        user1.setRole("ADMIN");
        user1.setCreatedAt(LocalDateTime.now());

        GetAllUserDTO user2 = new GetAllUserDTO();
        user2.setUserId(2);
        user2.setUsername("John");
        user2.setEmail("john@example.com");
        user2.setPassword("pass456");
        user2.setRole("PATIENT");
        user2.setCreatedAt(LocalDateTime.now());

        List<GetAllUserDTO> users = Arrays.asList(user1, user2);
        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/get-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Rizwan"))
                .andExpect(jsonPath("$[1].username").value("John"));
    }

    @Test
    void testDeleteAccount_Success() throws Exception {
        when(adminService.deleteAccount(anyInt())).thenReturn("User deleted successfully with ID: 5");

        mockMvc.perform(delete("/api/admin/delete-account/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("User account deleted successfully."));
    }


    @Test
    void testGetAllClaims_Success() throws Exception {
        GetAllClaimHistoryDTO claim = new GetAllClaimHistoryDTO(
                101,
                new BigDecimal("12000"),
                new BigDecimal("15000"),
                LocalDate.now(),
                "Diagnosis Test",
                "Treatment Type",
                "APPROVED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                501
        );

        when(adminService.getAllClaims()).thenReturn(List.of(claim));

        mockMvc.perform(get("/api/get-claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(101))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void testGetAllClaims_Failure() throws Exception {
        when(adminService.getAllClaims()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/get-claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
