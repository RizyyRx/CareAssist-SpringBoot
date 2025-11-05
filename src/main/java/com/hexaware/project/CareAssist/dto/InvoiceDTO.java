package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InvoiceDTO {
	
	@NotNull(message = "Patient ID is required")
    private Integer patientId;

    private BigDecimal consultationFee;
    private BigDecimal diagnosticTestsFee;
    private BigDecimal diagnosticScanFee;
    private BigDecimal medicationFee;

    public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public BigDecimal getConsultationFee() {
		return consultationFee;
	}

	public void setConsultationFee(BigDecimal consultationFee) {
		this.consultationFee = consultationFee;
	}

	public BigDecimal getDiagnosticTestsFee() {
		return diagnosticTestsFee;
	}

	public void setDiagnosticTestsFee(BigDecimal diagnosticTestsFee) {
		this.diagnosticTestsFee = diagnosticTestsFee;
	}

	public BigDecimal getDiagnosticScanFee() {
		return diagnosticScanFee;
	}

	public void setDiagnosticScanFee(BigDecimal diagnosticScanFee) {
		this.diagnosticScanFee = diagnosticScanFee;
	}

	public BigDecimal getMedicationFee() {
		return medicationFee;
	}

	public void setMedicationFee(BigDecimal medicationFee) {
		this.medicationFee = medicationFee;
	}


}
