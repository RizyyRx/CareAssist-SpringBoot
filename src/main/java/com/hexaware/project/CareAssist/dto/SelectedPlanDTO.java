package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SelectedPlanDTO {

	private int planId;
    private String planName;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
	private BigDecimal coverageBalance;
    private int policyTerm;
    private String description;
    private LocalDate startDate;
	private LocalDate endDate;
    private String status;
    
	public SelectedPlanDTO() {}

    public SelectedPlanDTO(int planId, String planName, BigDecimal coverageAmount, BigDecimal premiumAmount,
			BigDecimal coverageBalance, int policyTerm, String description, LocalDate startDate, LocalDate endDate,String status) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.coverageAmount = coverageAmount;
		this.premiumAmount = premiumAmount;
		this.coverageBalance = coverageBalance;
		this.policyTerm = policyTerm;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}
    
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
	
    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
	
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
