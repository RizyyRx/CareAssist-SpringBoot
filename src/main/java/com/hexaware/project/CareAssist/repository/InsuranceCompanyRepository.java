package com.hexaware.project.CareAssist.repository;

import com.hexaware.project.CareAssist.entity.InsuranceCompany;
import com.hexaware.project.CareAssist.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {
    Optional<InsuranceCompany> findByUserUsername(String username);
    Optional<InsuranceCompany> findByUserUserId(int userId);
    boolean existsByCompanyName(String companyName);
}
