package com.hexaware.project.CareAssist.service;

import java.util.List;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.GetAllPaymentDTO;
import com.hexaware.project.CareAssist.dto.GetAllUserDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.dto.SelectedPlanDTO;

public interface AdminService {

	List<GetAllUserDTO> getAllUsers();
	
	String deleteAccount(int userId);
	
	List<GetAllClaimHistoryDTO> getAllClaims();
	
	List<GetAllPaymentDTO> getAllPayments();
	
    List<GetAllPatientDTO> getAllPatients();

    List<GetAllPatientInsuranceDTO> getAllPatientInsurances();

    List<InvoiceViewDTO> getAllInvoices();

    List<SelectedPlanDTO> getPatientInsurancesByPatientId(int patientId);

    List<InvoiceViewDTO> getInvoicesByPatientId(int patientId);
}
