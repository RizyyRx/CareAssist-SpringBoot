package com.hexaware.project.CareAssist.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.InsuranceCompanyProfileDTO;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.InsuranceCompany;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.PatientInsurance;
import com.hexaware.project.CareAssist.entity.Payment;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.ClaimRepository;
import com.hexaware.project.CareAssist.repository.InsuranceCompanyRepository;
import com.hexaware.project.CareAssist.repository.InsurancePlanRepository;
import com.hexaware.project.CareAssist.repository.PatientInsuranceRepository;
import com.hexaware.project.CareAssist.repository.PaymentRepository;
import com.hexaware.project.CareAssist.repository.UserRepository;


@Service
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService{


		public InsuranceCompanyServiceImpl(InsurancePlanRepository insurancePlanRepository, ClaimRepository claimRepository,
			PaymentRepository paymentRepository, PatientInsuranceRepository patientInsuranceRepository,InsuranceCompanyRepository insuranceCompanyRepository, UserRepository userRepository) {
		super();
		this.insurancePlanRepository = insurancePlanRepository;
		this.claimRepository = claimRepository;
		this.paymentRepository = paymentRepository;
		this.patientInsuranceRepository = patientInsuranceRepository;
		this.insuranceCompanyRepository = insuranceCompanyRepository;
		this.userRepository = userRepository;
	}

		private InsurancePlanRepository insurancePlanRepository;
		private ClaimRepository claimRepository;
		private PaymentRepository paymentRepository;
		private PatientInsuranceRepository patientInsuranceRepository;
		private InsuranceCompanyRepository insuranceCompanyRepository;
		private UserRepository userRepository;
		

	    public String createInsurancePlan(User insuranceCompany,InsurancePlanDTO dto) {
	    	

	        InsurancePlan insurancePlan = new InsurancePlan();
	        insurancePlan.setInsuranceCompany(insuranceCompany);
	        insurancePlan.setPlanName(dto.getPlanName());
	        insurancePlan.setCoverageAmount(dto.getCoverageAmount());
	        insurancePlan.setPremiumAmount(dto.getPremiumAmount());
	        insurancePlan.setPolicyTerm(dto.getPolicyTerm());
	        insurancePlan.setDescription(dto.getDescription());
	        insurancePlanRepository.save(insurancePlan);

	        return "Insurance plan created successfully";
	    }
	    
		public List<InsurancePlanDTO> getAllInsurancePlans() {
	        List<InsurancePlan> plans = insurancePlanRepository.findAll();
	        return plans.stream().map(plan -> {
	            InsurancePlanDTO dto = new InsurancePlanDTO();
	            dto.setPlanId(plan.getPlanId());
	            dto.setPlanName(plan.getPlanName());
	            dto.setCoverageAmount(plan.getCoverageAmount());
	            dto.setPremiumAmount(plan.getPremiumAmount());
	            dto.setPolicyTerm(plan.getPolicyTerm());
	            dto.setDescription(plan.getDescription());
	            return dto;
	        }).toList();
	    }
		
		public String reviewAndApproveClaim(int claimId) {
	        Claim claim = claimRepository.findById(claimId)
	            .orElseThrow(() -> new RuntimeException("Claim not found"));
	        
	        Patient patient = claim.getPatient();
	        if (patient == null) {
	            throw new RuntimeException("Claim is not linked to a valid patient");
	        }
	        
	        PatientInsurance patientInsurance = patientInsuranceRepository
	                .findByPatientPatientIdAndStatus(patient.getPatientId(), "ACTIVE")
	                .orElseThrow(() -> new RuntimeException("No active insurance plan found for this patient"));
	        
	        BigDecimal currentBalance = patientInsurance.getCoverageBalance();
	        BigDecimal claimAmount = claim.getClaimAmount();
	        
	        if (currentBalance.compareTo(claimAmount) < 0) {
	            throw new RuntimeException("Insufficient coverage balance to approve this claim");
	        }
	        
	        BigDecimal newBalance = currentBalance.subtract(claimAmount);
	        patientInsurance.setCoverageBalance(newBalance);

	        claim.setStatus("APPROVED");
	        claim.setReviewedAt(LocalDateTime.now());
	        claim.setApprovedAt(LocalDateTime.now());

	        patientInsuranceRepository.save(patientInsurance);
	        claimRepository.save(claim);

	        return "Claim approved successfully and Coverage balance updated";
	    }
		
		public String processClaimPayment(User insuranceCompany, int claimId, BigDecimal amountPaid) {
		    Claim claim = claimRepository.findById(claimId)
		        .orElseThrow(() -> new RuntimeException("Claim not found"));

		    // Check if payment for claim already exists (one-to-one)
		    if (paymentRepository.existsByClaim(claim)) {
		        throw new RuntimeException("Payment for this claim already processed");
		    }

		    Payment payment = new Payment();
		    payment.setClaim(claim);
		    payment.setInsuranceCompany(insuranceCompany);
		    payment.setPatient(claim.getPatient());
		    payment.setAmountPaid(amountPaid);

		    paymentRepository.save(payment);

		    return "Payment processing done";
		}
		
		public List<GetAllClaimHistoryDTO> getClaimsByPatientId(int patientId) {
		    List<Claim> claims = claimRepository.findByPatientPatientId(patientId);
		    return claims.stream()
		        .map(c -> new GetAllClaimHistoryDTO(
		            c.getClaimId(),
		            c.getClaimAmount(),
		            c.getInvoiceAmount(),
		            c.getDateOfService(),
		            c.getDiagnosis(),
		            c.getTreatment(),
		            c.getStatus(),
		            c.getSubmittedAt(),
		            c.getReviewedAt(),
		            c.getApprovedAt(),
		            c.getPatient().getPatientId()
		        ))
		        .collect(Collectors.toList());
		}
		
		@Override
	    public InsuranceCompanyProfileDTO getProfile(User user) {
	        InsuranceCompany company = insuranceCompanyRepository.findByUserUserId(user.getUserId())
	                .orElse(null);

	        if (company == null) return null;

	        InsuranceCompanyProfileDTO dto = new InsuranceCompanyProfileDTO();
	        dto.setCompanyName(company.getCompanyName());
	        dto.setAddress(company.getAddress());
	        dto.setContactNumber(company.getContactNumber());
	        dto.setDescription(company.getDescription());
	        dto.setProfilePic(company.getProfilePic());
	        return dto;
	    }

	    @Override
	    public String updateProfile(User user, InsuranceCompanyProfileDTO dto) {
	        InsuranceCompany company = insuranceCompanyRepository.findByUserUserId(user.getUserId())
	                .orElse(new InsuranceCompany());

	        company.setUser(user);
	        company.setCompanyName(dto.getCompanyName());
	        company.setAddress(dto.getAddress());
	        company.setContactNumber(dto.getContactNumber());
	        company.setDescription(dto.getDescription());

	        insuranceCompanyRepository.save(company);
	        return "Insurance company profile updated successfully";
	    }

	    @Override
	    public String uploadProfilePic(User user, MultipartFile file) {
	        InsuranceCompany company = insuranceCompanyRepository.findByUserUserId(user.getUserId())
	                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

	        try {
	            String basePath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "profile-pics";
	            File dir = new File(basePath);
	            if (!dir.exists() && !dir.mkdirs()) {
	                throw new IOException("Failed to create upload directory: " + basePath);
	            }

	            String originalName = file.getOriginalFilename();
	            String safeName = (originalName != null) ? originalName.replaceAll("[^a-zA-Z0-9._-]", "_") : "unknown.png";
	            String filename = user.getUsername() + "_" + safeName;

	            File destination = new File(dir, filename);
	            file.transferTo(destination);

	            String relativePath = "/uploads/profile-pics/" + filename;
	            company.setProfilePic(relativePath);
	            insuranceCompanyRepository.save(company);

	            return relativePath;
	        } catch (IOException e) {
	            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
	        }
	    }

}
