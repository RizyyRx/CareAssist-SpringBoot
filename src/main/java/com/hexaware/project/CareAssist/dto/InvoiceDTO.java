package com.hexaware.project.CareAssist.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InvoiceDTO {
	
	@NotNull(message = "Patient ID is required")
    private Integer patientId;

    @Positive(message = "Consultation fee must be positive")
    private BigDecimal consultationFee;

    @Positive(message = "Diagnostic tests fee must be positive")
    private BigDecimal diagnosticTestsFee;

    @Positive(message = "Diagnostic scan fee must be positive")
    private BigDecimal diagnosticScanFee;

    @Positive(message = "Medication fee must be positive")
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
