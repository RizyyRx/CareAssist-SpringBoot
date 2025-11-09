package com.hexaware.project.CareAssist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{

	boolean existsByClaim(Claim claim);

	List<Payment> findByPatient(Patient patient);

	List<Payment> findByInsuranceCompanyUserId(int userId);
	
	
}
