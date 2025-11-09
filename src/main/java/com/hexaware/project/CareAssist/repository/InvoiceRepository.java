package com.hexaware.project.CareAssist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

	List<Invoice> findByPatient(Patient patient);
	
	List<Invoice> findByPatientPatientId(int patientId);

	List<Invoice> findByProviderUserId(int userId);
}
