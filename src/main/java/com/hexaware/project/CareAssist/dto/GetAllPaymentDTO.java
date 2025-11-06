package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GetAllPaymentDTO {

    private int paymentId;
    private int claimId;
    private int insuranceCompanyId;
    private int patientId;
    private BigDecimal amountPaid;
    private LocalDate paymentDate;

    // Constructors
    public GetAllPaymentDTO() {}

    public GetAllPaymentDTO(int paymentId, int claimId, int insuranceCompanyId, int patientId,
                      BigDecimal amountPaid, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.claimId = claimId;
        this.insuranceCompanyId = insuranceCompanyId;
        this.patientId = patientId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(int insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
