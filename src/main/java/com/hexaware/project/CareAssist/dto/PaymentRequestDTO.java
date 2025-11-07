package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PaymentRequestDTO {
	
	@NotNull(message = "Claim ID is required")
    private Integer claimId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment must be greater than 0")
    private BigDecimal amountPaid;

    public Integer getClaimId() {
		return claimId;
	}

	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

}
