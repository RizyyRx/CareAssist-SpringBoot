package com.hexaware.project.CareAssist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.dto.PaymentRequestDTO;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.UserRepository;
import com.hexaware.project.CareAssist.service.InsuranceCompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/insurance-company")
public class InsuranceCompanyController {

    public InsuranceCompanyController(InsuranceCompanyService insuranceCompanyService, UserRepository userRepository) {
		super();
		this.insuranceCompanyService = insuranceCompanyService;
		this.userRepository = userRepository;
	}


	private InsuranceCompanyService insuranceCompanyService;
    private UserRepository userRepository;

    // Create insurance plan
    @PreAuthorize("hasRole('INSURANCE_COMPANY')")
	@PostMapping("/create")
    public ResponseEntity<String> createInsurancePlan(@Valid @RequestBody InsurancePlanDTO insurancePlanDTO, Authentication authentication) {
		
		String username = authentication.getName(); // Extract from JWT
	    User user = userRepository.findByUsername(username)
	                 .orElseThrow(() -> new RuntimeException("User not found"));
	    
        String message =  insuranceCompanyService.createInsurancePlan(user, insurancePlanDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    
    // Get all insurance plans
    @PreAuthorize("hasAnyRole('INSURANCE_COMPANY','PATIENT')")
    @GetMapping("/get-all")
    public ResponseEntity<List<InsurancePlanDTO>> getAllInsurancePlans() {
        List<InsurancePlanDTO> plans = insuranceCompanyService.getAllInsurancePlans();
        return ResponseEntity.ok(plans);
    }
    
    // Approve claim
    @PreAuthorize("hasRole('INSURANCE_COMPANY')")
    @PatchMapping("/claim/approve/{claimId}")
    public ResponseEntity<String> approveClaim(@PathVariable int claimId) {
        String message = insuranceCompanyService.reviewAndApproveClaim(claimId);
        return ResponseEntity.ok(message);
    }
    
    // Process payment
    @PreAuthorize("hasRole('INSURANCE_COMPANY')")
    @PostMapping("/claim/process-payment")
    public ResponseEntity<String> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest,
                                                 Authentication authentication) {
        String username = authentication.getName();
        User insuranceCompany = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String result = insuranceCompanyService.processClaimPayment(
            insuranceCompany,
            paymentRequest.getClaimId(),
            paymentRequest.getAmountPaid(),
            paymentRequest.getTransactionRef());

        return ResponseEntity.ok(result);
    }
    
    // Get claim by patient Id
    @PreAuthorize("hasAnyRole('INSURANCE_COMPANY','PATIENT')")
    @GetMapping("/get-claims/patient/{patientId}")
    public ResponseEntity<List<GetAllClaimHistoryDTO>> getClaimsByPatient(@PathVariable int patientId) {
        List<GetAllClaimHistoryDTO> claimHistory = insuranceCompanyService.getClaimsByPatientId(patientId);
        return ResponseEntity.ok(claimHistory);
    }

}
