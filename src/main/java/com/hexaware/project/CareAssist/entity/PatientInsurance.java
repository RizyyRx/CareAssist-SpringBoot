package com.hexaware.project.CareAssist.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
@Table(name = "patient_insurances")
public class PatientInsurance {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientInsuranceId;

    @ManyToOne // Many patientInsurance can be there for one patient
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    @ManyToOne // Many patientInsurance can be there for one insurancePlan
    @JoinColumn(name = "plan_id", nullable = false)
    @NotNull(message = "Insurance plan is required")
    private InsurancePlan insurancePlan;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    @Column(nullable = false)
    private LocalDate endDate;
    
    @NotNull(message = "Coverage balance is required")
    @Positive(message = "Coverage balance must be positive")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal coverageBalance;

    @NotBlank(message = "Status is required")
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // e.g., ACTIVE, EXPIRED, CANCELLED
    
    // Auto-calculate endDate before insert
    @PrePersist
    public void calculateEndDate() {
        if (startDate != null && insurancePlan != null && insurancePlan.getPolicyTerm() > 0) {
            // Assuming policyTerm is in months
            this.endDate = startDate.plusMonths(insurancePlan.getPolicyTerm());
        }
    }
	
	public int getPatientInsuranceId() {
		return patientInsuranceId;
	}

	public void setPatientInsuranceId(int patientInsuranceId) {
		this.patientInsuranceId = patientInsuranceId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public InsurancePlan getInsurancePlan() {
		return insurancePlan;
	}

	public void setInsurancePlan(InsurancePlan insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public BigDecimal getCoverageBalance() {
		return coverageBalance;
	}

	public void setCoverageBalance(BigDecimal coverageBalance) {
		this.coverageBalance = coverageBalance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PatientInsurance() {
		super();
	}
	

	@Override
	public String toString() {
		return "PatientInsurance [patientInsuranceId=" + patientInsuranceId + ", patient=" + patient
				+ ", insurancePlan=" + insurancePlan + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", coverageBalance=" + coverageBalance + ", status=" + status + "]";
	}

	public PatientInsurance(int patientInsuranceId, @NotNull(message = "Patient is required") Patient patient,
			@NotNull(message = "Insurance plan is required") InsurancePlan insurancePlan, LocalDate startDate,
			@NotNull(message = "End date is required") @FutureOrPresent(message = "End date must be today or in the future") LocalDate endDate,
			@NotNull(message = "Coverage balance is required") @Positive(message = "Coverage balance must be positive") BigDecimal coverageBalance,
			@NotBlank(message = "Status is required") String status) {
		super();
		this.patientInsuranceId = patientInsuranceId;
		this.patient = patient;
		this.insurancePlan = insurancePlan;
		this.startDate = startDate;
		this.endDate = endDate;
		this.coverageBalance = coverageBalance;
		this.status = status;
	}


}
