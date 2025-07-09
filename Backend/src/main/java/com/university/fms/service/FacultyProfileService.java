package com.university.fms.service;

import com.university.fms.dto.request.FacultyProfileRequest;
import com.university.fms.dto.request.FacultyCreationRequest; // Import new DTO
import com.university.fms.entity.Department;
import com.university.fms.entity.FacultyProfile;
import com.university.fms.entity.User;
import com.university.fms.entity.Role; // Import Role entity
import com.university.fms.repository.DepartmentRepository;
import com.university.fms.repository.FacultyProfileRepository;
import com.university.fms.repository.RoleRepository; // Import RoleRepository
import com.university.fms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.university.fms.dto.response.FacultyProfileResponse;
import com.university.fms.dto.response.UserResponse;
import com.university.fms.dto.response.DepartmentResponse;
import org.springframework.security.crypto.password.PasswordEncoder; // Import for password hashing

import java.util.Collections; // For Collections.singleton
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacultyProfileService {

    @Autowired
    private FacultyProfileRepository facultyProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository; // Autowire RoleRepository

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowire PasswordEncoder

    // ... (getAllFacultyProfiles, getFacultyProfileById, getFacultyProfileByUserId,
    // getFacultyProfilesByDepartment, searchFacultyProfiles - remain the same,
    // as they return FacultyProfileResponse DTOs and handle lazy loading) ...

    public List<FacultyProfileResponse> getAllFacultyProfiles() {
        return facultyProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<FacultyProfileResponse> getFacultyProfileById(Long id) {
        return facultyProfileRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<FacultyProfileResponse> getFacultyProfileByUserId(Long userId) {
        return facultyProfileRepository.findByUserId(userId)
                .map(this::convertToDto);
    }

    public List<FacultyProfileResponse> getFacultyProfilesByDepartment(Long departmentId) {
        return facultyProfileRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FacultyProfileResponse> searchFacultyProfiles(String keyword) {
        return facultyProfileRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    /**
     * Creates a new User and a new Faculty Profile linked to that user.
     * @param request The FacultyCreationRequest DTO containing both user and faculty data.
     * @return The created FacultyProfileResponse DTO.
     */
    public FacultyProfileResponse createFacultyProfile(FacultyCreationRequest request) {
        // 1. Create a new User
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User newUser = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()) // Hash the password
        );

        // Assign 'FACULTY' role by default
        Role facultyRole = roleRepository.findByName("FACULTY")
                .orElseThrow(() -> new RuntimeException("Error: Role 'FACULTY' not found."));
        newUser.setRoles(new HashSet<>(Collections.singletonList(facultyRole)));

        User savedUser = userRepository.save(newUser); // Save the new user

        // 2. Create the Faculty Profile linked to the new user
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));

        FacultyProfile facultyProfile = new FacultyProfile();
        facultyProfile.setUser(savedUser); // Link to the newly created user
        facultyProfile.setFirstName(request.getFirstName());
        facultyProfile.setLastName(request.getLastName());
        facultyProfile.setBio(request.getBio());
        facultyProfile.setProfilePictureUrl(request.getProfilePictureUrl());
        facultyProfile.setDepartment(department);
        facultyProfile.setPhone(request.getPhone());
        facultyProfile.setOfficeLocation(request.getOfficeLocation());
        facultyProfile.setHireDate(request.getHireDate()); // Assuming hireDate is LocalDate in request

        // Important: Ensure `FacultyProfile` has `@OneToOne(cascade = CascadeType.ALL)`
        // or a similar cascade setup with `User` if you want deleting faculty to delete user.
        // For now, we are just saving.

        FacultyProfile savedFacultyProfile = facultyProfileRepository.save(facultyProfile);
        return convertToDto(savedFacultyProfile);
    }

    /**
     * Updates an existing faculty profile.
     * @param id The ID of the faculty profile to update.
     * @param request The FacultyProfileRequest DTO containing updated data.
     * @return The updated FacultyProfileResponse DTO.
     */
    public FacultyProfileResponse updateFacultyProfile(Long id, FacultyProfileRequest request) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty profile not found with id: " + id));

        // Note: User ID cannot be changed via this update method.
        // If user details need updating, it should be done through a separate user management API.

        facultyProfile.setFirstName(request.getFirstName());
        facultyProfile.setLastName(request.getLastName());
        facultyProfile.setBio(request.getBio());
        facultyProfile.setProfilePictureUrl(request.getProfilePictureUrl());
        facultyProfile.setPhone(request.getPhone());
        facultyProfile.setOfficeLocation(request.getOfficeLocation());
        facultyProfile.setHireDate(request.getHireDate());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
            facultyProfile.setDepartment(department);
        } else {
            facultyProfile.setDepartment(null); // Allow setting department to null
        }

        FacultyProfile updatedFacultyProfile = facultyProfileRepository.save(facultyProfile);
        return convertToDto(updatedFacultyProfile);
    }

    /**
     * Deletes a faculty profile by ID.
     * IMPORTANT: Consider if this should also delete the associated user.
     * If User and FacultyProfile have CascadeType.ALL on User side, deleting FacultyProfile might also delete User.
     * For now, it only deletes the faculty profile.
     * @param id The ID of the faculty profile to delete.
     */
    public void deleteFacultyProfile(Long id) {
        FacultyProfile facultyProfile = facultyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty profile not found with id: " + id));

        // If you want to delete the user when faculty profile is deleted,
        // ensure cascade is configured on the User entity for the facultyProfile field,
        // or manually delete the user here:
        // userRepository.delete(facultyProfile.getUser());

        facultyProfileRepository.delete(facultyProfile);
    }

    /**
     * Helper method to convert FacultyProfile entity to FacultyProfileResponse DTO.
     * This handles initializing lazy loaded relationships (user, department) within the transactional context
     * before mapping them to their respective DTOs.
     * @param faculty The FacultyProfile entity.
     * @return The FacultyProfileResponse DTO.
     */
    private FacultyProfileResponse convertToDto(FacultyProfile faculty) {
        // Access lazy-loaded fields within this @Transactional method to initialize them
        User user = faculty.getUser();
        Department department = faculty.getDepartment();

        return new FacultyProfileResponse(
            faculty.getId(),
            user,
            faculty.getFirstName(),
            faculty.getLastName(),
            faculty.getBio(),
            faculty.getProfilePictureUrl(),
            department,
            faculty.getPhone(),
            faculty.getOfficeLocation(),
            faculty.getHireDate(),
            faculty.getCreatedAt(),
            faculty.getUpdatedAt()
        );
    }
}
