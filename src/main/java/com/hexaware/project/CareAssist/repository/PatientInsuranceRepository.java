package com.hexaware.project.CareAssist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.PatientInsurance;

public interface PatientInsuranceRepository extends JpaRepository<PatientInsurance, Integer>{

	List<PatientInsurance> findByPatientPatientId(int patientId);
	
	Optional<PatientInsurance> findByPatient(Patient patient);
}
