package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class InsurancePlanDTO {
	
	@NotNull(message = "Plan id is required")
	private int planId;

    @NotBlank(message = "Plan name is required")
    @Size(max = 30, message = "Plan name must be within 30 characters")
    private String planName;

    @NotNull(message = "Coverage amount is required")
    @Positive(message = "Coverage amount must be positive")
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium amount is required")
    @Positive(message = "Premium amount must be positive")
    private BigDecimal premiumAmount;

    @Positive(message = "Coverage balance must be positive")
    private BigDecimal coverageBalance;

    @NotNull(message = "Policy term is required")
    @Positive(message = "Policy term must be positive")
    private Integer policyTerm;

    @Size(max = 1000, message = "Description must be within 1000 characters")
    private String description; // Optional
    
	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public BigDecimal getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(BigDecimal coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

    public BigDecimal getCoverageBalance() {
		return coverageBalance;
	}

	public void setCoverageBalance(BigDecimal coverageBalance) {
		this.coverageBalance = coverageBalance;
	}
	
	public Integer getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
