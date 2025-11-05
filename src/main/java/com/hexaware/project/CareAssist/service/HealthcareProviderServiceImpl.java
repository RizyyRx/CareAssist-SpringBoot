package com.hexaware.project.CareAssist.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hexaware.project.CareAssist.dto.InvoiceDTO;
import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.repository.InvoiceRepository;
import com.hexaware.project.CareAssist.repository.PatientRepository;

@Service
public class HealthcareProviderServiceImpl implements HealthcareProviderService{

    public HealthcareProviderServiceImpl(PatientRepository patientRepository, InvoiceRepository invoiceRepository) {
		super();
		this.patientRepository = patientRepository;
		this.invoiceRepository = invoiceRepository;
	}

	private PatientRepository patientRepository;
    private InvoiceRepository invoiceRepository;
	
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

}
