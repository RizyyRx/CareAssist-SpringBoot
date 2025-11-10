package com.hexaware.project.CareAssist.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientId;

    @OneToOne // One patient can be one user only
    @JoinColumn(name = "user_id", nullable = true, unique = true)
    private User user;
    

	private String firstName;
	private String lastName;

    private LocalDate dob;

    private String gender;

    @Column(length = 15, unique = true)
    private String contactNumber;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    
    @Column(name = "profile_pic")
    private String profilePic;

    // One patient will have many patientInsurance records
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PatientInsurance> patientInsurance;

    // One patient will have many invoices
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Invoice> invoice;
	
	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public List<PatientInsurance> getPatientInsurance() {
		return patientInsurance;
	}

	public void setPatientInsurance(List<PatientInsurance> patientInsurance) {
		this.patientInsurance = patientInsurance;
	}

	public List<Invoice> getInvoice() {
		return invoice;
	}

	public void setInvoice(List<Invoice> invoice) {
		this.invoice = invoice;
	}

	public String getProfilePic() {
	    return profilePic;
	}

	public void setProfilePic(String profilePic) {
	    this.profilePic = profilePic;
	}
	
	public Patient() {
		super();
	}

	public Patient(int patientId, User user, String firstName, String lastName,
			LocalDate dob, String gender, String contactNumber,
			String address, String medicalHistory, List<PatientInsurance> patientInsurance, List<Invoice> invoice) {
		super();
		this.patientId = patientId;
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.address = address;
		this.medicalHistory = medicalHistory;
		this.patientInsurance = patientInsurance;
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "Patient [patientId=" + patientId + ", user=" + user + ", firstName=" + firstName + ", lastName="
				+ lastName + ", dob=" + dob + ", gender=" + gender + ", contactNumber=" + contactNumber + ", address="
				+ address + ", medicalHistory=" + medicalHistory + ", patientInsurance=" + patientInsurance
				+ ", invoice=" + invoice + "]";
	}



}
