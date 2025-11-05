package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceViewDTO {
	
	private int invoiceId;
    private BigDecimal consultationFee;
    private BigDecimal diagnosticScanFee;
    private BigDecimal diagnosticTestsFee;
    private BigDecimal medicationFee;
    private LocalDate dueDate;
    private LocalDate invoiceDate;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    private String patientName;
    private String providerName;
	

	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public BigDecimal getConsultationFee() {
		return consultationFee;
	}
	public void setConsultationFee(BigDecimal consultationFee) {
		this.consultationFee = consultationFee;
	}
	public BigDecimal getDiagnosticScanFee() {
		return diagnosticScanFee;
	}
	public void setDiagnosticScanFee(BigDecimal diagnosticScanFee) {
		this.diagnosticScanFee = diagnosticScanFee;
	}
	public BigDecimal getDiagnosticTestsFee() {
		return diagnosticTestsFee;
	}
	public void setDiagnosticTestsFee(BigDecimal diagnosticTestsFee) {
		this.diagnosticTestsFee = diagnosticTestsFee;
	}
	public BigDecimal getMedicationFee() {
		return medicationFee;
	}
	public void setMedicationFee(BigDecimal medicationFee) {
		this.medicationFee = medicationFee;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	public InvoiceViewDTO() {
		super();
	}

	public InvoiceViewDTO(int invoiceId, BigDecimal consultationFee, BigDecimal diagnosticScanFee,
			BigDecimal diagnosticTestsFee, BigDecimal medicationFee, LocalDate dueDate, LocalDate invoiceDate,
			String status, BigDecimal subtotal, BigDecimal tax, BigDecimal totalAmount, String patientName,
			String providerName) {
		super();
		this.invoiceId = invoiceId;
		this.consultationFee = consultationFee;
		this.diagnosticScanFee = diagnosticScanFee;
		this.diagnosticTestsFee = diagnosticTestsFee;
		this.medicationFee = medicationFee;
		this.dueDate = dueDate;
		this.invoiceDate = invoiceDate;
		this.status = status;
		this.subtotal = subtotal;
		this.tax = tax;
		this.totalAmount = totalAmount;
		this.patientName = patientName;
		this.providerName = providerName;
	}


}
