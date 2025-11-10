package com.hexaware.project.CareAssist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class HealthcareProviderProfileDTO {

    @NotBlank(message = "Provider name cannot be blank")
    @Size(max = 100, message = "Provider name cannot exceed 100 characters")
    private String providerName;

    @NotBlank(message = "Specialization cannot be blank")
    @Size(max = 80, message = "Specialization cannot exceed 80 characters")
    private String specialization;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @NotBlank(message = "Contact number cannot be blank")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Contact number must be between 10 and 15 digits")
    private String contactNumber;

    @Size(max = 300, message = "Description cannot exceed 300 characters")
    private String description;

    private String profilePic;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
