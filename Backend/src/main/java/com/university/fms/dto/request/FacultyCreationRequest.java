package com.university.fms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;       // Ensure these imports are present
import lombok.Setter;       // Ensure these imports are present
import lombok.NoArgsConstructor; // Ensure these imports are present
import lombok.AllArgsConstructor; // Ensure these imports are present
import java.time.LocalDate;

@Getter // This annotation tells Lombok to generate all getters
@Setter // This annotation tells Lombok to generate all setters
@NoArgsConstructor // This annotation tells Lombok to generate a no-argument constructor
@AllArgsConstructor // This annotation tells Lombok to generate a constructor with all arguments
public class FacultyCreationRequest {

    // User Creation Fields
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    // Faculty Profile Fields
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    private String bio;

    @Size(max = 255, message = "Profile picture URL cannot exceed 255 characters")
    private String profilePictureUrl;

    @NotNull(message = "Department is required")
    private Long departmentId; // Department is required for new faculty

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Office location cannot exceed 100 characters")
    private String officeLocation;

    private LocalDate hireDate;

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProfilePictureUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBio() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOfficeLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalDate getHireDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public CharSequence getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getDepartmentId() {
		// TODO Auto-generated method stub
		return null;
	}
}
