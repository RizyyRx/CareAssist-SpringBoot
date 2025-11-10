package com.hexaware.project.CareAssist.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.InsuranceCompanyProfileDTO;
import com.hexaware.project.CareAssist.dto.InsurancePlanDTO;
import com.hexaware.project.CareAssist.entity.InsuranceCompany;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.User;

public interface InsuranceCompanyService {

	String createInsurancePlan(User insuranceCompany,InsurancePlanDTO dto);
	
	List<InsurancePlanDTO> getAllInsurancePlans();
	
	String reviewAndApproveClaim(int claimId);
	
	String processClaimPayment(User insuranceCompany, int claimId, BigDecimal amountPaid);
	
	List<GetAllClaimHistoryDTO> getClaimsByPatientId(int patientId);
	
	InsuranceCompanyProfileDTO  getProfile(User user);
    
	String updateProfile(User user, InsuranceCompanyProfileDTO dto);
    
    String uploadProfilePic(User user, MultipartFile file);
}
