package com.hexaware.project.CareAssist.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.Payment;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.ClaimRepository;
import com.hexaware.project.CareAssist.repository.InsurancePlanRepository;
import com.hexaware.project.CareAssist.repository.PaymentRepository;


@Service
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService{


		public InsuranceCompanyServiceImpl(InsurancePlanRepository insurancePlanRepository, ClaimRepository claimRepository,
			PaymentRepository paymentRepository) {
		super();
		this.insurancePlanRepository = insurancePlanRepository;
		this.claimRepository = claimRepository;
		this.paymentRepository = paymentRepository;
	}

		private InsurancePlanRepository insurancePlanRepository;
		private ClaimRepository claimRepository;
		private PaymentRepository paymentRepository;

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

	        claim.setStatus("APPROVED");
	        claim.setReviewedAt(LocalDateTime.now());
	        claim.setApprovedAt(LocalDateTime.now());

	        claimRepository.save(claim);

	        return "Claim approved successfully";
	    }
		
		public String processClaimPayment(User insuranceCompany, int claimId, BigDecimal amountPaid, String transactionRef) {
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
		    payment.setTransactionRef(transactionRef);

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
		            c.getApprovedAt()
		        ))
		        .collect(Collectors.toList());
		}

}
