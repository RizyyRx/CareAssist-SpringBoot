package com.hexaware.project.CareAssist.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne(optional = false) // One payment for one claim (must exist)
    @JoinColumn(name = "claim_id", nullable = false, unique = true)
    private Claim claim;

    @ManyToOne(optional = true) // Many payments for one insuranceCompany
    @JoinColumn(name = "insurance_company_id", nullable = true)
    private User insuranceCompany;

    @ManyToOne(optional = true) // Many payments for one patient
    @JoinColumn(name = "patient_id", nullable = true)
    private Patient patient;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate paymentDate;

    public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public User getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(User insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
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

	public Payment() {
		super();
	}

	public Payment(int paymentId, Claim claim, User insuranceCompany, Patient patient,
			BigDecimal amountPaid, LocalDate paymentDate) {
		super();
		this.paymentId = paymentId;
		this.claim = claim;
		this.insuranceCompany = insuranceCompany;
		this.patient = patient;
		this.amountPaid = amountPaid;
		this.paymentDate = paymentDate;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", claim=" + claim + ", insuranceCompany=" + insuranceCompany
				+ ", patient=" + patient + ", amountPaid=" + amountPaid + ", paymentDate=" + paymentDate+ "]";
	}

}
