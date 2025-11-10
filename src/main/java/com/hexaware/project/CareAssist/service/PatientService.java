package com.hexaware.project.CareAssist.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.ClaimSubmissionDTO;
import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllPaymentDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.dto.PatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.PatientUpdateDTO;
import com.hexaware.project.CareAssist.dto.SelectedPlanDTO;
import com.hexaware.project.CareAssist.entity.User;

public interface PatientService {

	PatientUpdateDTO getPatientProfile(User user);
	
	String updatePatientProfile(User user, PatientUpdateDTO patientUpdateDTO);
	
	String uploadProfilePic(User user, MultipartFile file);
	
	String selectInsurancePlan(User user, PatientInsuranceDTO patientInsuranceDTO);
	
	List<SelectedPlanDTO> getSelectedPlans(User user);
	
	List<InvoiceViewDTO> getInvoices(User user);
	
	String submitClaim(User user, ClaimSubmissionDTO dto);
	
	String markInvoiceAsPaid(int invoiceId, User user);
	
	List<GetAllClaimHistoryDTO> getClaims(User user);
	
	List<GetAllPaymentDTO> getPaymentsForPatient(User user); 

}
