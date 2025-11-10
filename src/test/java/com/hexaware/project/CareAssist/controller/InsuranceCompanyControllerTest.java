package com.hexaware.project.CareAssist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.dto.PaymentRequestDTO;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.UserRepository;
import com.hexaware.project.CareAssist.service.InsuranceCompanyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceCompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class InsuranceCompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InsuranceCompanyService insuranceCompanyService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private com.hexaware.project.CareAssist.jwt.JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser() {
        User user = new User();
        user.setUsername("insureCo");
        user.setEmail("insure@gmail.com");
        return user;
    }

    @Test
    void testCreateInsurancePlan_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("insureCo");

        InsurancePlanDTO dto = new InsurancePlanDTO();
        dto.setPlanName("HealthSecure");
        dto.setCoverageAmount(new BigDecimal("10000"));
        dto.setPremiumAmount(new BigDecimal("500"));
        dto.setPolicyTerm(12);
        dto.setDescription("Basic health plan");

        when(userRepository.findByUsername("insureCo")).thenReturn(Optional.of(mockUser()));
        when(insuranceCompanyService.createInsurancePlan(any(User.class), any(InsurancePlanDTO.class)))
                .thenReturn("Plan created successfully");

        mockMvc.perform(post("/api/insurance-company/create")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Plan created successfully"));
    }

    @Test
    void testCreateInsurancePlan_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("unknown");

        InsurancePlanDTO dto = new InsurancePlanDTO();
        dto.setPlanName("HealthSecure");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/insurance-company/create")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testApproveClaim_Success() throws Exception {
        int claimId = 1;
        when(insuranceCompanyService.reviewAndApproveClaim(claimId))
                .thenReturn("Claim approved successfully");

        mockMvc.perform(patch("/api/insurance-company/claim/approve/{claimId}", claimId))
                .andExpect(status().isOk())
                .andExpect(content().string("Claim approved successfully"));
    }

    @Test
    void testApproveClaim_Failure() throws Exception {
        int claimId = 999;
        when(insuranceCompanyService.reviewAndApproveClaim(claimId))
                .thenThrow(new RuntimeException("Claim not found"));

        mockMvc.perform(patch("/api/insurance-company/claim/approve/{claimId}", claimId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Claim not found"));
    }

    @Test
    void testProcessPayment_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("insureCo");

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setClaimId(10);
        paymentRequest.setAmountPaid(new BigDecimal("7500"));

        when(userRepository.findByUsername("insureCo")).thenReturn(Optional.of(mockUser()));
        when(insuranceCompanyService.processClaimPayment(any(User.class), eq(10), eq(new BigDecimal("7500"))))
                .thenReturn("Payment processed successfully");

        mockMvc.perform(post("/api/insurance-company/claim/process-payment")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully"));
    }

    @Test
    void testProcessPayment_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ghost");

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setClaimId(99);
        paymentRequest.setAmountPaid(new BigDecimal("2000"));

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/insurance-company/claim/process-payment")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }
}
