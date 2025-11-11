package com.hexaware.project.CareAssist.service;



import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hexaware.project.CareAssist.dto.ClaimSubmissionDTO;
import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllPaymentDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.dto.PatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.PatientUpdateDTO;
import com.hexaware.project.CareAssist.dto.SelectedPlanDTO;
import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.PatientInsurance;
import com.hexaware.project.CareAssist.entity.Payment;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.ClaimRepository;
import com.hexaware.project.CareAssist.repository.InsurancePlanRepository;
import com.hexaware.project.CareAssist.repository.InvoiceRepository;
import com.hexaware.project.CareAssist.repository.PatientInsuranceRepository;
import com.hexaware.project.CareAssist.repository.PatientRepository;
import com.hexaware.project.CareAssist.repository.PaymentRepository;

@Service
public class PatientServiceImpl implements PatientService{
	

	public PatientServiceImpl(PatientRepository patientRepository, InsurancePlanRepository insurancePlanRepository,
			PatientInsuranceRepository patientInsuranceRepository, InvoiceRepository invoiceRepository, ClaimRepository claimRepository, PaymentRepository paymentRepository) {
		super();
		this.patientRepository = patientRepository;
		this.insurancePlanRepository = insurancePlanRepository;
		this.patientInsuranceRepository = patientInsuranceRepository;
		this.invoiceRepository = invoiceRepository;
		this.claimRepository = claimRepository;
		this.paymentRepository = paymentRepository;
	}

	private PatientRepository patientRepository;
	private InsurancePlanRepository insurancePlanRepository;
	private PatientInsuranceRepository patientInsuranceRepository;
	private InvoiceRepository invoiceRepository;
	private ClaimRepository claimRepository;
	private PaymentRepository paymentRepository;


	public String updatePatientProfile(User user, PatientUpdateDTO dto) {

		// If patient record is present, use that to update, else creates new patient object and sets the record
	    Patient patient = patientRepository.findByUser(user)
	                      .orElse(new Patient()); // create if not exists
	    patient.setUser(user);
	    patient.setFirstName(dto.getFirstName());
	    patient.setLastName(dto.getLastName());
	    patient.setDob(dto.getDob());
	    patient.setGender(dto.getGender());
	    patient.setContactNumber(dto.getContactNumber());
	    patient.setAddress(dto.getAddress());
	    patient.setMedicalHistory(dto.getMedicalHistory());

	    patientRepository.save(patient);

	    return "Patient profile updated successfully" ;
	}
	
	public PatientUpdateDTO getPatientProfile(User user) {
		Patient patient = patientRepository.findByUser(user)
	            .orElse(null);
		
		if (patient == null) {
	        return null;
	    }
		
	    PatientUpdateDTO dto = new PatientUpdateDTO();
	    dto.setFirstName(patient.getFirstName());
	    dto.setLastName(patient.getLastName());
	    dto.setDob(patient.getDob());
	    dto.setGender(patient.getGender());
	    dto.setContactNumber(patient.getContactNumber());
	    dto.setAddress(patient.getAddress());
	    dto.setMedicalHistory(patient.getMedicalHistory());
	    dto.setProfilePic(patient.getProfilePic());

	    return dto;
	}
	
	public String uploadProfilePic(User user, MultipartFile file) {
        Patient patient = patientRepository.findByUserUserId(user.getUserId())
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
            patient.setProfilePic(relativePath);
            patientRepository.save(patient);

            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
        }
    }


	public String selectInsurancePlan(User user, PatientInsuranceDTO patientInsuranceDTO) {
		
		Patient patient = user.getPatient();
		if (patient == null) {
	        throw new RuntimeException("No patient found for user, Kindly update your patient profile");
	    }
		
		Optional<PatientInsurance> existingPlan = patientInsuranceRepository.findByPatient(patient);
	    if (existingPlan.isPresent()) {
	        throw new RuntimeException("Patient already has an active insurance plan");
	    }
		
		InsurancePlan insurancePlan = insurancePlanRepository.findById(patientInsuranceDTO.getPlanId())
				.orElseThrow(() -> new RuntimeException("Insurance Plan not found"));
		
		
		PatientInsurance patientInsurance = new PatientInsurance();
		
		patientInsurance.setPatient(patient);
		patientInsurance.setInsurancePlan(insurancePlan);
		patientInsurance.setCoverageBalance(insurancePlan.getCoverageAmount());
		
	    // Manually set startDate so @PrePersist can use it
	    patientInsurance.setStartDate(LocalDate.now());
		
		patientInsuranceRepository.save(patientInsurance);
		
		return "Insurance plan selected successfully";
		
	}
	
	public List<SelectedPlanDTO> getSelectedPlans(User user) {
		Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No patient found for user"));

        List<PatientInsurance> insurances = patientInsuranceRepository.findByPatientPatientId(patient.getPatientId());

        return insurances.stream().map(pi -> new SelectedPlanDTO(
                pi.getInsurancePlan().getPlanId(),
                pi.getInsurancePlan().getPlanName(),
                pi.getInsurancePlan().getCoverageAmount(),
                pi.getInsurancePlan().getPremiumAmount(),
                pi.getCoverageBalance(),
                pi.getInsurancePlan().getPolicyTerm(),
                pi.getInsurancePlan().getDescription(),
                pi.getStartDate(),
                pi.getEndDate(),
                pi.getStatus()
        )).collect(Collectors.toList());

	}
	
	public List<InvoiceViewDTO> getInvoices(User user) {
	    Patient patient = user.getPatient();
	    if (patient == null) {
	        throw new RuntimeException("No patient found for user");
	    }
	    List<Invoice> invoices = invoiceRepository.findByPatient(patient);

	    return invoices.stream()
	        .map(inv -> {
	            InvoiceViewDTO dto = new InvoiceViewDTO();
	            dto.setInvoiceId(inv.getInvoiceId());
	            dto.setConsultationFee(inv.getConsultationFee());
	            dto.setDiagnosticScanFee(inv.getDiagnosticScanFee());
	            dto.setDiagnosticTestsFee(inv.getDiagnosticTestsFee());
	            dto.setMedicationFee(inv.getMedicationFee());
	            dto.setDueDate(inv.getDueDate());
	            dto.setInvoiceDate(inv.getInvoiceDate());
	            dto.setStatus(inv.getStatus());
	            dto.setSubtotal(inv.getSubtotal());
	            dto.setTax(inv.getTax());
	            dto.setTotalAmount(inv.getTotalAmount());
	            dto.setPatientName(
	                    inv.getPatient() != null && inv.getPatient().getUser() != null
	                        ? inv.getPatient().getUser().getUsername()
	                        : "Unknown Patient"
	                );

	                dto.setProviderName(
	                    inv.getProvider() != null
	                        ? inv.getProvider().getUsername()
	                        : "Deleted Provider"
	                );
	            return dto;
	        })
	        .collect(Collectors.toList());
	}
	
	public String submitClaim(User user, ClaimSubmissionDTO dto) {
        Patient patient = user.getPatient();
        if (patient == null) {
            throw new RuntimeException("No patient found for user");
        }

        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        InsurancePlan insurancePlan = insurancePlanRepository.findById(dto.getInsurancePlanId())
                .orElseThrow(() -> new RuntimeException("Insurance plan not found"));
        
        PatientInsurance patientInsurance = patientInsuranceRepository
                .findByPatientPatientIdAndStatus(patient.getPatientId(), "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No active insurance plan found for this patient"));
        
        if(patientInsurance.getCoverageBalance().compareTo(invoice.getTotalAmount()) < 0) {
        	 throw new RuntimeException("Not enough balance for this claim.");
        }
        
        claimRepository.findByInvoice(invoice)
        .ifPresent(c -> { throw new RuntimeException("You have already used this invoice for a claim."); });

        Claim claim = new Claim();
        claim.setPatient(patient);
        claim.setInvoice(invoice);
        claim.setInsurancePlan(insurancePlan);

        claim.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        claim.setPatientDob(patient.getDob());
        claim.setPatientAddress(patient.getAddress());

        claim.setDiagnosis(dto.getDiagnosis());
        claim.setTreatment(dto.getTreatment());
        claim.setDateOfService(dto.getDateOfService());

        // Fetch invoice amount directly from invoice entity
        claim.setInvoiceAmount(invoice.getTotalAmount());
        claim.setClaimAmount(invoice.getTotalAmount());

        claim.setStatus("SUBMITTED");

        claimRepository.save(claim);

        return "Claim submitted successfully";
    }
	
	public List<GetAllClaimHistoryDTO> getClaims(User user) {
	    Patient patient = user.getPatient();
	    if (patient == null) {
	        throw new RuntimeException("No patient found for user");
	    }

	    List<Claim> claims = claimRepository.findByPatient(patient);

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

	
	public String markInvoiceAsPaid(int invoiceId, User user) {
	    Patient patient = user.getPatient();
	    if (patient == null) {
	        throw new RuntimeException("No patient found for user");
	    }

	    Invoice invoice = invoiceRepository.findById(invoiceId)
	        .orElseThrow(() -> new RuntimeException("Invoice not found"));

	    if (invoice.getPatient().getPatientId() != patient.getPatientId()) {
	        throw new RuntimeException("Invoice does not belong to the patient");
	    }

	    invoice.setStatus("PAID");
	    invoiceRepository.save(invoice);

	    return "Invoice marked as PAID";
	}
	
	public List<GetAllPaymentDTO> getPaymentsForPatient(User user) {
	    Patient patient = user.getPatient();
	    if (patient == null) {
	        throw new RuntimeException("No patient found for user");
	    }

	    List<Payment> payments = paymentRepository.findByPatient(patient);

	    return payments.stream()
	            .map(p -> new GetAllPaymentDTO(
	                    p.getPaymentId(),
	                    p.getClaim().getClaimId(),
	                    p.getInsuranceCompany() != null ? p.getInsuranceCompany().getUserId() : 0,
	                    p.getPatient().getPatientId(),
	                    p.getAmountPaid(),
	                    p.getPaymentDate()
	            ))
	            .collect(Collectors.toList());
	}



}
