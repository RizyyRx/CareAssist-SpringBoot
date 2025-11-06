package com.hexaware.project.CareAssist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.GetAllPaymentDTO;
import com.hexaware.project.CareAssist.dto.GetAllUserDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.service.AdminService;

@RestController
@RequestMapping("/api")
public class AdminController {


	private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-users")
    public ResponseEntity<List<GetAllUserDTO>> getAllUsers() {
        List<GetAllUserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get all claims
    @PreAuthorize("hasAnyRole('ADMIN','INSURANCE_COMPANY')")
    @GetMapping("/get-claims")
    public ResponseEntity<List<GetAllClaimHistoryDTO>> getAllClaims() {
        List<GetAllClaimHistoryDTO> claims = adminService.getAllClaims();
        return ResponseEntity.ok(claims);
    }
    
    // Get all payments
    @PreAuthorize("hasAnyRole('ADMIN','INSURANCE_COMPANY')")
    @GetMapping("/get-payments")
    public ResponseEntity<List<GetAllPaymentDTO>> getAllPayments() {
        List<GetAllPaymentDTO> payments = adminService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    // Get all patients
    @PreAuthorize("hasAnyRole('ADMIN', 'HEALTHCARE_PROVIDER', 'INSURANCE_COMPANY')")
    @GetMapping("/get-patients")
    public ResponseEntity<List<GetAllPatientDTO>> getAllPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }
    
    // Get all Patient Insurances
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-patient-insurances")
    public ResponseEntity<List<GetAllPatientInsuranceDTO>> getAllPatientInsurances() {
        return ResponseEntity.ok(adminService.getAllPatientInsurances());
    }

    //Get all invoices
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-invoices")
    public ResponseEntity<List<InvoiceViewDTO>> getAllInvoices() {
        return ResponseEntity.ok(adminService.getAllInvoices());
    }

    // Get patient insurance by patient Id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-patient-insurance/{patientId}")
    public ResponseEntity<List<GetAllPatientInsuranceDTO>> getPatientInsurancesByPatientId(@PathVariable int patientId) {
        return ResponseEntity.ok(adminService.getPatientInsurancesByPatientId(patientId));
    }

    // Get invoice by patient Id
    @PreAuthorize("hasAnyRole('ADMIN','HEALTHCARE_PROVIDER')")
    @GetMapping("/get-invoice/{patientId}")
    public ResponseEntity<List<InvoiceViewDTO>> getInvoicesByPatientId(@PathVariable int patientId) {
        return ResponseEntity.ok(adminService.getInvoicesByPatientId(patientId));
    }
}
