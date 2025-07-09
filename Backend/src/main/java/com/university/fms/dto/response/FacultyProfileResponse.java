package com.university.fms.dto.response;

import java.time.LocalDate; // Or java.util.Date or whatever you use for hireDate

public class FacultyProfileResponse {
    private Long id;
    private UserResponse user; // Reference to UserResponse DTO
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    private DepartmentResponse department; // Reference to DepartmentResponse DTO
    private String phone;
    private String officeLocation;
    private LocalDate hireDate; // Use LocalDate if that's your backend entity type for date
    private String createdAt; // Keep String for Date/Time from Entity
    private String updatedAt; // Keep String for Date/Time from Entity

    // Constructor from FacultyProfile entity
    public FacultyProfileResponse(Long id, com.university.fms.entity.User user, String firstName, String lastName,
                                  String bio, String profilePictureUrl, com.university.fms.entity.Department department,
                                  String phone, String officeLocation, LocalDate hireDate, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
        this.id = id;
        // IMPORTANT: Map entity to DTO
        this.user = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        // IMPORTANT: Map entity to DTO
        this.department = department != null ? new DepartmentResponse(department.getId(), department.getName()) : null;
        this.phone = phone;
        this.officeLocation = officeLocation;
        this.hireDate = hireDate;
        this.createdAt = createdAt != null ? createdAt.toString() : null; // Convert LocalDateTime to String
        this.updatedAt = updatedAt != null ? updatedAt.toString() : null; // Convert LocalDateTime to String
    }

    // Getters for all fields
    public Long getId() { return id; }
    public UserResponse getUser() { return user; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBio() { return bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public DepartmentResponse getDepartment() { return department; }
    public String getPhone() { return phone; }
    public String getOfficeLocation() { return officeLocation; }
    public LocalDate getHireDate() { return hireDate; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters (optional for response DTOs)
    public void setId(Long id) { this.id = id; }
    public void setUser(UserResponse user) { this.user = user; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public void setDepartment(DepartmentResponse department) { this.department = department; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}