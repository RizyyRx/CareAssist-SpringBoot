package com.hexaware.project.CareAssist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.project.CareAssist.entity.Patient;
import com.hexaware.project.CareAssist.entity.User;

public interface PatientRepository extends JpaRepository<Patient, Integer>{

	Optional<Patient> findByUser(User user);
	
	Optional<Patient> findByUserUserId(int userId);
}
