package com.hexaware.project.CareAssist.service;

import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.HealthcareProviderProfileDTO;
import com.hexaware.project.CareAssist.dto.InvoiceDTO;
import com.hexaware.project.CareAssist.entity.User;

public interface HealthcareProviderService {

	String createInvoice(User provider, InvoiceDTO dto);
	
	HealthcareProviderProfileDTO getProfile(User user);
	
	String updateProfile(User user, HealthcareProviderProfileDTO dto);
	
	String uploadProfilePic(User user, MultipartFile file);
}
