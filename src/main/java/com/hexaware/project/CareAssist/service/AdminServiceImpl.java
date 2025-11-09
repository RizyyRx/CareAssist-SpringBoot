package com.hexaware.project.CareAssist.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hexaware.project.CareAssist.dto.GetAllClaimHistoryDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientDTO;
import com.hexaware.project.CareAssist.dto.GetAllPatientInsuranceDTO;
import com.hexaware.project.CareAssist.dto.GetAllPaymentDTO;
import com.hexaware.project.CareAssist.dto.GetAllUserDTO;
import com.hexaware.project.CareAssist.dto.InvoiceViewDTO;
import com.hexaware.project.CareAssist.entity.Claim;
import com.hexaware.project.CareAssist.entity.Payment;
import com.hexaware.project.CareAssist.entity.User;
import com.hexaware.project.CareAssist.exception.ResourceNotFoundException;
import com.hexaware.project.CareAssist.repository.ClaimRepository;
import com.hexaware.project.CareAssist.repository.InvoiceRepository;
import com.hexaware.project.CareAssist.repository.PatientInsuranceRepository;
import com.hexaware.project.CareAssist.repository.PatientRepository;
import com.hexaware.project.CareAssist.repository.PaymentRepository;
import com.hexaware.project.CareAssist.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService{


	public AdminServiceImpl(UserRepository userRepository, ClaimRepository claimRepository,
			PaymentRepository paymentRepository, PatientRepository patientRepository,
			PatientInsuranceRepository patientInsuranceRepository, InvoiceRepository invoiceRepository) {
		super();
		this.userRepository = userRepository;
		this.claimRepository = claimRepository;
		this.paymentRepository = paymentRepository;
		this.patientRepository = patientRepository;
		this.patientInsuranceRepository = patientInsuranceRepository;
		this.invoiceRepository = invoiceRepository;
	}


	private UserRepository userRepository;
	private ClaimRepository claimRepository;
	private PaymentRepository paymentRepository;
	private PatientRepository patientRepository;
	private PatientInsuranceRepository patientInsuranceRepository;
	private InvoiceRepository invoiceRepository;

	public List<GetAllUserDTO> getAllUsers() {
	    List<User> users = userRepository.findAll();

	    return users.stream().map(user -> {
	        GetAllUserDTO dto = new GetAllUserDTO();
	        dto.setUserId(user.getUserId());
	        dto.setUsername(user.getUsername());
	        dto.setEmail(user.getEmail());
	        dto.setCreatedAt(user.getCreatedAt());

	        // Extract single role name (if exists)
	        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
	            String roleName = user.getRoles().iterator().next().getName();
	            dto.setRole(roleName);
	        } else {
	            dto.setRole("N/A");
	        }

	        return dto;
	    }).collect(Collectors.toList());
	}


	@Transactional
	public String deleteAccount(int userId) {
	    User user = userRepository.findByUserId(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

	    user.getRoles().clear();

	    if (user.getPatient() != null) {
	        user.getPatient().setUser(null);
	        user.setPatient(null);
	    }

	    if (user.getInsurancePlan() != null && !user.getInsurancePlan().isEmpty()) {
	        user.getInsurancePlan().forEach(plan -> plan.setInsuranceCompany(null));
	    }

	    userRepository.save(user);
	    userRepository.delete(user);

	    return "User deleted successfully with ID: " + userId;
	}


	
	public List<GetAllClaimHistoryDTO> getAllClaims() {
	    List<Claim> claims = claimRepository.findAll();

	    return claims.stream()
	        .map(c -> new GetAllClaimHistoryDTO(
	            c.getClaimId(),
	            c.getClaimAmount(),
	            c.getInvoiceAmount(),
	            c.getDateOfService(),
	            c.getDiagnosis(),
	            c.getTreatment(),
	            c.getStatus(),
	            c.getSubmittedAt(),
	            c.getReviewedAt(),
	            c.getApprovedAt()
	        ))
	        .collect(Collectors.toList());
	}
	
	public List<GetAllPaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
            .map(p -> new GetAllPaymentDTO(
                p.getPaymentId(),
                p.getClaim().getClaimId(),
                p.getInsuranceCompany().getUserId(),
                p.getPatient().getPatientId(),
                p.getAmountPaid(),
                p.getPaymentDate()
            )).collect(Collectors.toList());
    }
	
	
    public List<GetAllPatientDTO> getAllPatients() {
        return patientRepository.findAll()
            .stream()
            .map(p -> new GetAllPatientDTO(
                p.getPatientId(),
                p.getUser().getUserId(),
                p.getFirstName(),
                p.getLastName(),
                p.getDob(),
                p.getGender(),
                p.getContactNumber(),
                p.getAddress(),
                p.getMedicalHistory()
            )).toList();
    }

    
    public List<GetAllPatientInsuranceDTO> getAllPatientInsurances() {
        return patientInsuranceRepository.findAll()
            .stream()
            .map(pi -> new GetAllPatientInsuranceDTO(
                pi.getPatientInsuranceId(),
                pi.getPatient().getPatientId(),
                pi.getInsurancePlan().getPlanId(),
                pi.getStartDate(),
                pi.getEndDate(),
                pi.getStatus()
            )).toList();
    }

    
    public List<InvoiceViewDTO> getAllInvoices() {
        return invoiceRepository.findAll()
            .stream()
            .map(inv -> new InvoiceViewDTO(
            		inv.getInvoiceId(),
                    inv.getConsultationFee(),                 
                    inv.getDiagnosticScanFee(),               
                    inv.getDiagnosticTestsFee(),              
                    inv.getMedicationFee(),                   
                    inv.getDueDate(),                         
                    inv.getInvoiceDate(),                     
                    inv.getStatus(),                          
                    inv.getSubtotal(),                        
                    inv.getTax(),                             
                    inv.getTotalAmount(),                     
                    inv.getPatient().getUser().getUsername(), 
                    inv.getProvider().getUsername()   
            )).toList();
    }

    
    public List<GetAllPatientInsuranceDTO> getPatientInsurancesByPatientId(int patientId) {
        return patientInsuranceRepository.findByPatientPatientId(patientId)
            .stream()
            .map(pi -> new GetAllPatientInsuranceDTO(
                pi.getPatientInsuranceId(),
                pi.getPatient().getPatientId(),
                pi.getInsurancePlan().getPlanId(),
                pi.getStartDate(),
                pi.getEndDate(),
                pi.getStatus()
            )).toList();
    }

    
    public List<InvoiceViewDTO> getInvoicesByPatientId(int patientId) {
        return invoiceRepository.findByPatientPatientId(patientId)
            .stream()
            .map(inv -> new InvoiceViewDTO(
            		inv.getInvoiceId(),
                    inv.getConsultationFee(),                 
                    inv.getDiagnosticScanFee(),               
                    inv.getDiagnosticTestsFee(),              
                    inv.getMedicationFee(),                   
                    inv.getDueDate(),                         
                    inv.getInvoiceDate(),                     
                    inv.getStatus(),                          
                    inv.getSubtotal(),                        
                    inv.getTax(),                             
                    inv.getTotalAmount(),                     
                    inv.getPatient().getUser().getUsername(), 
                    inv.getProvider().getUsername()           

            )).toList();
    }
	
}
