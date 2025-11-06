package com.hexaware.project.CareAssist.service;

import java.math.BigDecimal;
import java.util.List;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.User;

public interface InsuranceCompanyService {

	String createInsurancePlan(User insuranceCompany,InsurancePlanDTO dto);
	
	List<InsurancePlanDTO> getAllInsurancePlans();
	
	String reviewAndApproveClaim(int claimId);
	
	String processClaimPayment(User insuranceCompany, int claimId, BigDecimal amountPaid);
	
	List<GetAllClaimHistoryDTO> getClaimsByPatientId(int patientId);
	
}
