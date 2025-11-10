package com.hexaware.project.CareAssist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.Invoice;
import com.hexaware.project.CareAssist.entity.Patient;

public interface ClaimRepository extends JpaRepository<Claim, Integer>{

	 List<Claim> findByPatientPatientId(int patientId);
	 List<Claim> findByPatient(Patient patient);
	Optional<Claim> findByInvoice(Invoice invoice);
	
}
