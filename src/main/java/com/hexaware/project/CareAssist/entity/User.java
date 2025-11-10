package com.hexaware.project.CareAssist.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(unique = true,length = 20)
	private String username;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	// One user can be one patient only
	@OneToOne(mappedBy = "user",cascade = CascadeType.PERSIST) 
	private Patient patient;
	
	// One user can have many insurancePlans
	@OneToMany(mappedBy = "insuranceCompany", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<InsurancePlan> insurancePlan;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	 
	private Set<Role> roles;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public List<InsurancePlan> getInsurancePlan() {
		return insurancePlan;
	}

	public void setInsurancePlan(List<InsurancePlan> insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public User() {
		super();
	}

	public User(int userId, String username, String email, String password, LocalDateTime createdAt, Patient patient,
			List<InsurancePlan> insurancePlan, Set<Role> roles) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
		this.patient = patient;
		this.insurancePlan = insurancePlan;
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", createdAt=" + createdAt + ", patient=" + patient + ", insurancePlan=" + insurancePlan + ", roles="
				+ roles + "]";
	}



}
