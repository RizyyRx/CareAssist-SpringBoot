package com.hexaware.project.CareAssist.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.HealthcareProviderProfileDTO;
import com.hexaware.project.CareAssist.dto.InvoiceDTO;
import com.hexaware.project.CareAssist.entity.HealthcareProvider;
import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.HealthcareProviderRepository;
import com.hexaware.project.CareAssist.repository.InvoiceRepository;
import com.hexaware.project.CareAssist.repository.PatientRepository;

@Service
public class HealthcareProviderServiceImpl implements HealthcareProviderService{

    public HealthcareProviderServiceImpl(PatientRepository patientRepository, InvoiceRepository invoiceRepository, HealthcareProviderRepository healthcareProviderRepository) {
		super();
		this.patientRepository = patientRepository;
		this.invoiceRepository = invoiceRepository;
		this.healthcareProviderRepository = healthcareProviderRepository;
	}

	private PatientRepository patientRepository;
    private InvoiceRepository invoiceRepository;
    private HealthcareProviderRepository healthcareProviderRepository;
	
	public String createInvoice(User provider, InvoiceDTO dto) {
		Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

	    Invoice invoice = new Invoice();
	    invoice.setProvider(provider);
	    invoice.setPatient(patient);
	    invoice.setInvoiceDate(LocalDate.now());
	    invoice.setConsultationFee(dto.getConsultationFee());
	    invoice.setDiagnosticTestsFee(dto.getDiagnosticTestsFee());
	    invoice.setDiagnosticScanFee(dto.getDiagnosticScanFee());
	    invoice.setMedicationFee(dto.getMedicationFee());
	
	    // totals auto calculated by entity @PrePersist/@PreUpdate
	
	    invoiceRepository.save(invoice);
	
	    return "Invoice created successfully";
	}
	
    @Override
    public HealthcareProviderProfileDTO getProfile(User user) {
        HealthcareProvider provider = healthcareProviderRepository.findByUserUserId(user.getUserId())
                .orElse(null);

        if (provider == null) return null;

        HealthcareProviderProfileDTO dto = new HealthcareProviderProfileDTO();
        dto.setProviderName(provider.getProviderName());
        dto.setSpecialization(provider.getSpecialization());
        dto.setAddress(provider.getAddress());
        dto.setContactNumber(provider.getContactNumber());
        dto.setDescription(provider.getDescription());
        dto.setProfilePic(provider.getProfilePic());
        return dto;
    }

    @Override
    public String updateProfile(User user, HealthcareProviderProfileDTO dto) {
        HealthcareProvider provider = healthcareProviderRepository.findByUserUserId(user.getUserId())
                .orElse(new HealthcareProvider());

        provider.setUser(user);
        provider.setProviderName(dto.getProviderName());
        provider.setSpecialization(dto.getSpecialization());
        provider.setAddress(dto.getAddress());
        provider.setContactNumber(dto.getContactNumber());
        provider.setDescription(dto.getDescription());

        healthcareProviderRepository.save(provider);
        return "Healthcare provider profile updated successfully";
    }

    @Override
    public String uploadProfilePic(User user, MultipartFile file) {
        HealthcareProvider provider = healthcareProviderRepository.findByUserUserId(user.getUserId())
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
            provider.setProfilePic(relativePath);
            healthcareProviderRepository.save(provider);

            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
        }
    }


}
