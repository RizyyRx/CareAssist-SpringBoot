package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GetAllClaimHistoryDTO {
	
	private int claimId;
	private int patientId;
	private BigDecimal claimAmount;
    private BigDecimal invoiceAmount;
    private LocalDate dateOfService;
    private String diagnosis;
    private String treatment;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;

    public int getClaimId() {
		return claimId;
	}
	public void setClaimId(int claimId) {
		this.claimId = claimId;
	}
    public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public LocalDate getDateOfService() {
		return dateOfService;
	}
	public void setDateOfService(LocalDate dateOfService) {
		this.dateOfService = dateOfService;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getTreatment() {
		return treatment;
	}
	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}
	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}
	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}
	public LocalDateTime getApprovedAt() {
		return approvedAt;
	}
	public void setApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}
	public GetAllClaimHistoryDTO() {
		super();
	}
	public GetAllClaimHistoryDTO(int claimId, BigDecimal claimAmount, BigDecimal invoiceAmount, LocalDate dateOfService,
	        String diagnosis, String treatment, String status, LocalDateTime submittedAt, 
	        LocalDateTime reviewedAt, LocalDateTime approvedAt, int patientId) {
	    this.claimId = claimId;
	    this.claimAmount = claimAmount;
	    this.invoiceAmount = invoiceAmount;
	    this.dateOfService = dateOfService;
	    this.diagnosis = diagnosis;
	    this.treatment = treatment;
	    this.status = status;
	    this.submittedAt = submittedAt;
	    this.reviewedAt = reviewedAt;
	    this.approvedAt = approvedAt;
	    this.patientId = patientId;
	}

}
