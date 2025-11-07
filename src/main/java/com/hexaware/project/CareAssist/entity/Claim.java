package com.hexaware.project.CareAssist.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "claims")
public class Claim {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int claimId;

    @ManyToOne // Many claims for one patient
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne // one claim for one invoice 
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne // Many claims for one insurancePlan
    @JoinColumn(name = "insurance_plan_id", nullable = false)
    private InsurancePlan insurancePlan;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private LocalDate patientDob;

    @Column(nullable = false)
    private String patientAddress;

    // Medical service details
    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false)
    private String treatment;

    @Column(nullable = false)
    private LocalDate dateOfService;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal claimAmount;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    

    public int getClaimId() {
		return claimId;
	}

	public void setClaimId(int claimId) {
		this.claimId = claimId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public InsurancePlan getInsurancePlan() {
		return insurancePlan;
	}

	public void setInsurancePlan(InsurancePlan insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public LocalDate getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(LocalDate patientDob) {
		this.patientDob = patientDob;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
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

	public LocalDate getDateOfService() {
		return dateOfService;
	}

	public void setDateOfService(LocalDate dateOfService) {
		this.dateOfService = dateOfService;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
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

	public Claim() {
		super();
	}

	public Claim(int claimId, Patient patient,
			Invoice invoice,
			InsurancePlan insurancePlan,
			String patientName,
			LocalDate patientDob,
			String patientAddress,
			String diagnosis,
			String treatment,
			LocalDate dateOfService,
			BigDecimal invoiceAmount,
			BigDecimal claimAmount,
			String status, LocalDateTime submittedAt,
			LocalDateTime reviewedAt, LocalDateTime approvedAt) {
		super();
		this.claimId = claimId;
		this.patient = patient;
		this.invoice = invoice;
		this.insurancePlan = insurancePlan;
		this.patientName = patientName;
		this.patientDob = patientDob;
		this.patientAddress = patientAddress;
		this.diagnosis = diagnosis;
		this.treatment = treatment;
		this.dateOfService = dateOfService;
		this.invoiceAmount = invoiceAmount;
		this.claimAmount = claimAmount;
		this.status = status;
		this.submittedAt = submittedAt;
		this.reviewedAt = reviewedAt;
		this.approvedAt = approvedAt;
	}

	@Override
	public String toString() {
		return "Claim [claimId=" + claimId + ", patient=" + patient + ", invoice=" + invoice + ", insurancePlan="
				+ insurancePlan + ", patientName=" + patientName + ", patientDob=" + patientDob + ", patientAddress="
				+ patientAddress + ", diagnosis=" + diagnosis + ", treatment=" + treatment + ", dateOfService="
				+ dateOfService + ", invoiceAmount=" + invoiceAmount + ", claimAmount=" + claimAmount + ", status="
				+ status + ", submittedAt=" + submittedAt + ", reviewedAt=" + reviewedAt + ", approvedAt=" + approvedAt+ "]";
	}


}
