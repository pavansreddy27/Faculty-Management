package com.university.fms.controller;

// import com.university.fms.dto.request.FacultyProfileRequest; // Keep for PUT
import com.university.fms.dto.request.FacultyProfileRequest; // Keep for update
import com.university.fms.dto.request.FacultyCreationRequest; // IMPORTANT: New import for POST
import com.university.fms.dto.response.ApiResponse;
import com.university.fms.dto.response.FacultyProfileResponse;
import com.university.fms.service.FacultyProfileService;
import jakarta.validation.Valid;
import com.university.fms.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/faculty")
public class FacultyProfileController {

    @Autowired
    private FacultyProfileService facultyProfileService;

    // ... (getAllFacultyProfiles, getFacultyProfileById, getFacultyProfileByUserId,
    // getFacultyProfilesByDepartment, searchFacultyProfiles - remain the same,
    // as they already return FacultyProfileResponse DTOs) ...

    /**
     * Retrieves all faculty profiles. Accessible by ADMIN, HR, FACULTY, STUDENT roles.
     * Returns a list of FacultyProfileResponse DTOs.
     * @return ResponseEntity with ApiResponse containing a list of faculty profiles.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<FacultyProfileResponse>>> getAllFacultyProfiles() {
        try {
            List<FacultyProfileResponse> profiles = facultyProfileService.getAllFacultyProfiles();
            return ResponseEntity.ok(ApiResponse.success("Faculty profiles retrieved successfully", profiles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve faculty profiles: " + e.getMessage()));
        }
    }

    /**
     * Retrieves a single faculty profile by ID. Accessible by ADMIN, HR, FACULTY, STUDENT roles.
     * Returns a FacultyProfileResponse DTO.
     * @param id The ID of the faculty profile.
     * @return ResponseEntity with ApiResponse containing the faculty profile.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ApiResponse<FacultyProfileResponse>> getFacultyProfileById(@PathVariable Long id) {
        try {
            Optional<FacultyProfileResponse> profile = facultyProfileService.getFacultyProfileById(id);
            if (profile.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Faculty profile retrieved successfully", profile.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body(ApiResponse.error("Faculty profile not found with id: " + id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve faculty profile: " + e.getMessage()));
        }
    }

    /**
     * Retrieves a faculty profile by associated user ID. Accessible by ADMIN, HR, FACULTY, STUDENT roles.
     * Returns a FacultyProfileResponse DTO.
     * @param userId The ID of the user associated with the faculty profile.
     * @return ResponseEntity with ApiResponse containing the faculty profile.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ApiResponse<FacultyProfileResponse>> getFacultyProfileByUserId(@PathVariable Long userId) {
        try {
            Optional<FacultyProfileResponse> profile = facultyProfileService.getFacultyProfileByUserId(userId);
            if (profile.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Faculty profile retrieved successfully", profile.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body(ApiResponse.error("Faculty profile not found for user id: " + userId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve faculty profile by user ID: " + e.getMessage()));
        }
    }

    /**
     * Retrieves faculty profiles by department ID. Accessible by ADMIN, HR, FACULTY, STUDENT roles.
     * Returns a list of FacultyProfileResponse DTOs.
     * @param departmentId The ID of the department.
     * @return ResponseEntity with ApiResponse containing a list of faculty profiles.
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<FacultyProfileResponse>>> getFacultyProfilesByDepartment(@PathVariable Long departmentId) {
        try {
            List<FacultyProfileResponse> profiles = facultyProfileService.getFacultyProfilesByDepartment(departmentId);
            return ResponseEntity.ok(ApiResponse.success("Faculty profiles retrieved successfully", profiles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve faculty profiles by department: " + e.getMessage()));
        }
    }

    /**
     * Searches for faculty profiles by keyword. Accessible by ADMIN, HR, FACULTY, STUDENT roles.
     * Returns a list of FacultyProfileResponse DTOs.
     * @param keyword The search keyword.
     * @return ResponseEntity with ApiResponse containing a list of faculty profiles.
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'FACULTY', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<FacultyProfileResponse>>> searchFacultyProfiles(@RequestParam String keyword) {
        try {
            List<FacultyProfileResponse> profiles = facultyProfileService.searchFacultyProfiles(keyword);
            return ResponseEntity.ok(ApiResponse.success("Faculty profiles retrieved successfully", profiles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search faculty profiles: " + e.getMessage()));
        }
    }

    /**
     * Creates a new faculty profile AND a new associated user. Accessible by ADMIN or HR roles.
     * Accepts FacultyCreationRequest DTO.
     * @param request The request body containing faculty profile and new user data.
     * @return ResponseEntity with ApiResponse containing the created faculty profile.
     */
    @PostMapping // This method now accepts FacultyCreationRequest
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<ApiResponse<FacultyProfileResponse>> createFacultyProfile(@Valid @RequestBody FacultyCreationRequest request) {
        try {
            FacultyProfileResponse profile = facultyProfileService.createFacultyProfile(request);
            return new ResponseEntity<>(ApiResponse.success("Faculty profile and user created successfully", profile), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create faculty profile: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred during faculty profile creation: " + e.getMessage()));
        }
    }

    /**
     * Updates an existing faculty profile. Accessible by ADMIN, HR, or FACULTY (if it's their own profile).
     * Accepts FacultyProfileRequest DTO (for update).
     * @param id The ID of the faculty profile to update.
     * @param request The request body containing updated faculty profile data.
     * @return ResponseEntity with ApiResponse containing the updated faculty profile.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<FacultyProfileResponse>> updateFacultyProfile(@PathVariable Long id,
                                                                           @Valid @RequestBody FacultyProfileRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FACULTY"))) {

                UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
                Long authenticatedUserId = userDetails.getId();

                Optional<FacultyProfileResponse> existingProfileOptional = facultyProfileService.getFacultyProfileById(id);

                if (existingProfileOptional.isPresent()) {
                    FacultyProfileResponse existingProfile = existingProfileOptional.get();
                    if (existingProfile.getUser() == null || !existingProfile.getUser().getId().equals(authenticatedUserId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                               .body(ApiResponse.error("You are not authorized to update this faculty profile."));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(ApiResponse.error("Faculty profile not found with id: " + id));
                }
            }

            FacultyProfileResponse profile = facultyProfileService.updateFacultyProfile(id, request);
            return ResponseEntity.ok(ApiResponse.success("Faculty profile updated successfully", profile));
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update faculty profile: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred during faculty profile update: " + e.getMessage()));
        }
    }

    /**
     * Deletes a faculty profile by ID. Accessible only by ADMIN role.
     * @param id The ID of the faculty profile to delete.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFacultyProfile(@PathVariable Long id) {
        try {
            facultyProfileService.deleteFacultyProfile(id);
            return ResponseEntity.ok(ApiResponse.success("Faculty profile deleted successfully", null));
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete faculty profile: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred during faculty profile deletion: " + e.getMessage()));
        }
    }
}