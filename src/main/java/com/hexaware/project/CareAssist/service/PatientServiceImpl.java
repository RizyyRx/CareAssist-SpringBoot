package com.hexaware.project.CareAssist.service;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hexaware.project.CareAssist.dto.ClaimSubmissionDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.dto.PatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.PatientUpdateDTO;
import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.InsurancePlan;
import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.PatientInsurance;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.ClaimRepository;
import com.hexaware.project.CareAssist.repository.InsurancePlanRepository;
import com.hexaware.project.CareAssist.repository.InvoiceRepository;
import com.hexaware.project.CareAssist.repository.PatientInsuranceRepository;
import com.hexaware.project.CareAssist.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{
	

	public PatientServiceImpl(PatientRepository patientRepository, InsurancePlanRepository insurancePlanRepository,
			PatientInsuranceRepository patientInsuranceRepository, InvoiceRepository invoiceRepository, ClaimRepository claimRepository) {
		super();
		this.patientRepository = patientRepository;
		this.insurancePlanRepository = insurancePlanRepository;
		this.patientInsuranceRepository = patientInsuranceRepository;
		this.invoiceRepository = invoiceRepository;
		this.claimRepository = claimRepository;
	}

	private PatientRepository patientRepository;
	private InsurancePlanRepository insurancePlanRepository;
	private PatientInsuranceRepository patientInsuranceRepository;
	private InvoiceRepository invoiceRepository;
	private ClaimRepository claimRepository;


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

	    return "Patient profile updated successfully for user: " + user.getUsername();
	}
	
	public PatientUpdateDTO getPatientProfile(User user) {
		Patient patient = patientRepository.findByUser(user)
	            .orElse(null);
		
		if (patient == null) {
	        throw new RuntimeException("No patient profile found for user: " + user.getUsername());
	    }
		
	    PatientUpdateDTO dto = new PatientUpdateDTO();
	    dto.setFirstName(patient.getFirstName());
	    dto.setLastName(patient.getLastName());
	    dto.setDob(patient.getDob());
	    dto.setGender(patient.getGender());
	    dto.setContactNumber(patient.getContactNumber());
	    dto.setAddress(patient.getAddress());
	    dto.setMedicalHistory(patient.getMedicalHistory());

	    return dto;
	}


	public String selectInsurancePlan(User user, PatientInsuranceDTO patientInsuranceDTO) {
		
		//user has linked patient entity
		Patient patient = user.getPatient();
		if (patient == null) {
	        throw new RuntimeException("No patient found for user");
	    }
		
		InsurancePlan insurancePlan = insurancePlanRepository.findById(patientInsuranceDTO.getPlanId())
				.orElseThrow(() -> new RuntimeException("Insurance Plan not found"));
		
		
		PatientInsurance patientInsurance = new PatientInsurance();
		
		patientInsurance.setPatient(patient);
		patientInsurance.setInsurancePlan(insurancePlan);
		
	    // Manually set startDate so @PrePersist can use it
	    patientInsurance.setStartDate(LocalDate.now());
		
		patientInsuranceRepository.save(patientInsurance);
		
		return "Insurance plan selected successfully";
		
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
	            dto.setInvoiceNumber(inv.getInvoiceNumber());
	            dto.setStatus(inv.getStatus());
	            dto.setSubtotal(inv.getSubtotal());
	            dto.setTax(inv.getTax());
	            dto.setTotalAmount(inv.getTotalAmount());
	            dto.setPatientId(inv.getPatient().getPatientId());
	            dto.setProviderId(inv.getProvider().getUserId());
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
        claim.setClaimAmount(dto.getClaimAmount());

        claim.setMedicalDocuments(dto.getMedicalDocuments());

        claim.setStatus("SUBMITTED");

        claimRepository.save(claim);

        return "Claim submitted successfully";
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

}
