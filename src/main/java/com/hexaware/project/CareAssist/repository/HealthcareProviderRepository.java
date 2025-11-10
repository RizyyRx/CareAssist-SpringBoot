package com.hexaware.project.CareAssist.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hexaware.project.CareAssist.entity.HealthcareProvider;

public interface HealthcareProviderRepository extends JpaRepository<HealthcareProvider, Long> {
    Optional<HealthcareProvider> findByUserUserId(int userId);
    Optional<HealthcareProvider> findByUserUsername(String username);
}
