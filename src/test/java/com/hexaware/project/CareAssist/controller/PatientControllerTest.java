package com.hexaware.project.CareAssist.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaware.project.CareAssist.dto.*;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.UserRepository;
import com.hexaware.project.CareAssist.service.PatientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

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
        user.setUsername("rizwan");
        user.setEmail("rizwan@gmail.com");
        return user;
    }

    @Test
    void testSelectInsurancePlan_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("rizwan");

        PatientInsuranceDTO dto = new PatientInsuranceDTO();
        dto.setPlanId(1);

        when(userRepository.findByUsername("rizwan")).thenReturn(Optional.of(mockUser()));
        when(patientService.selectInsurancePlan(any(User.class), any(PatientInsuranceDTO.class)))
                .thenReturn("Insurance plan selected successfully");

        mockMvc.perform(post("/api/patient/select-plan")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Insurance plan selected successfully"));
    }

    @Test
    void testSelectInsurancePlan_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("unknown");

        PatientInsuranceDTO dto = new PatientInsuranceDTO();
        dto.setPlanId(1);

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/patient/select-plan")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testGetSelectedPlans_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("rizwan");

        SelectedPlanDTO plan1 = new SelectedPlanDTO(
                1,
                "HealthPlus",
                new BigDecimal("10000"),
                new BigDecimal("500"),
                new BigDecimal("8000"),
                12,
                "Comprehensive Health Plan",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1),
                "ACTIVE"
        );

        when(userRepository.findByUsername("rizwan"))
                .thenReturn(Optional.of(mockUser()));

        when(patientService.getSelectedPlans(any(User.class)))
                .thenReturn(List.of(plan1));

        mockMvc.perform(get("/api/patient/selected-plans").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value("HealthPlus"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void testSubmitClaim_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("rizwan");

        ClaimSubmissionDTO claimDto = new ClaimSubmissionDTO();
        claimDto.setInvoiceId(10);
        claimDto.setInsurancePlanId(2);
        claimDto.setDiagnosis("Surgery");
        claimDto.setTreatment("Heart Bypass");
        claimDto.setDateOfService(LocalDate.now());

        when(userRepository.findByUsername("rizwan")).thenReturn(Optional.of(mockUser()));
        when(patientService.submitClaim(any(User.class), any(ClaimSubmissionDTO.class)))
                .thenReturn("Claim submitted successfully");

        mockMvc.perform(post("/api/patient/submit-claim")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Claim submitted successfully"));
    }

    @Test
    void testSubmitClaim_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ghost");

        ClaimSubmissionDTO claimDto = new ClaimSubmissionDTO();
        claimDto.setInvoiceId(5);
        claimDto.setInsurancePlanId(1);
        claimDto.setDiagnosis("Fever");
        claimDto.setTreatment("Basic Care");
        claimDto.setDateOfService(LocalDate.now());

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/patient/submit-claim")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }
}
