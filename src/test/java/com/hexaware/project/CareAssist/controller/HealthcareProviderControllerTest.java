package com.hexaware.project.CareAssist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaware.project.CareAssist.dto.HealthcareProviderProfileDTO;
import com.hexaware.project.CareAssist.dto.InvoiceDTO;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.UserRepository;
import com.hexaware.project.CareAssist.service.HealthcareProviderService;

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

@WebMvcTest(HealthcareProviderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class HealthcareProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HealthcareProviderService healthcareProviderService;

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
        user.setUsername("provider");
        user.setEmail("provider@gmail.com");
        return user;
    }

    @Test
    void testCreateInvoice_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("provider");

        InvoiceDTO dto = new InvoiceDTO();
        dto.setPatientId(1);
        dto.setConsultationFee(new BigDecimal("500"));
        dto.setDiagnosticTestsFee(new BigDecimal("1000"));
        dto.setDiagnosticScanFee(new BigDecimal("1500"));
        dto.setMedicationFee(new BigDecimal("800"));

        when(userRepository.findByUsername("provider")).thenReturn(Optional.of(mockUser()));
        when(healthcareProviderService.createInvoice(any(User.class), any(InvoiceDTO.class)))
                .thenReturn("Invoice created successfully");

        mockMvc.perform(post("/api/provider/create-invoice")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Invoice created successfully"));
    }

    @Test
    void testCreateInvoice_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ghost");

        InvoiceDTO dto = new InvoiceDTO();
        dto.setPatientId(1);
        dto.setConsultationFee(new BigDecimal("500"));

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/provider/create-invoice")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProfile_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("provider");

        HealthcareProviderProfileDTO profileDTO = new HealthcareProviderProfileDTO();
        profileDTO.setProviderName("Dr. Rizwan");
        profileDTO.setSpecialization("Cardiology");
        profileDTO.setAddress("Chennai");
        profileDTO.setContactNumber("9876543210");
        profileDTO.setDescription("Experienced cardiologist");
        profileDTO.setProfilePic("profile.jpg");

        when(userRepository.findByUsername("provider")).thenReturn(Optional.of(mockUser()));
        when(healthcareProviderService.getProfile(any(User.class))).thenReturn(profileDTO);

        mockMvc.perform(get("/api/provider/profile").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providerName").value("Dr. Rizwan"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.contactNumber").value("9876543210"))
                .andExpect(jsonPath("$.address").value("Chennai"))
                .andExpect(jsonPath("$.profilePic").value("profile.jpg"));
    }

    @Test
    void testGetProfile_UserNotFound() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("ghost");

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/provider/profile").principal(authentication))
                .andExpect(status().isBadRequest());
    }
}
