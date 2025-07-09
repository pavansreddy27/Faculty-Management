package com.university.fms.controller;

import com.university.fms.dto.request.PublicationRequest;
import com.university.fms.dto.response.ApiResponse;
import com.university.fms.dto.response.PublicationResponseDto;
import com.university.fms.entity.Publication; // Keep if your service methods still work with entities internally before converting
import com.university.fms.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublicationResponseDto>>> getAllPublications() {
        try {
            // Service method should return List<PublicationResponseDto>
            List<PublicationResponseDto> publications = publicationService.getAllPublications();
            return ResponseEntity.ok(ApiResponse.success("Publications retrieved successfully", publications));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging, consider a proper logger in production
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve publications: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublicationResponseDto>> getPublicationById(@PathVariable Long id) {
        try {
            // Service method should return Optional<PublicationResponseDto>
            Optional<PublicationResponseDto> publicationDto = publicationService.getPublicationById(id);
            if (publicationDto.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Publication retrieved successfully", publicationDto.get()));
            } else {
                return ResponseEntity.status(404)
                                     .body(ApiResponse.error("Publication with ID " + id + " not found"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve publication: " + e.getMessage()));
        }
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<ApiResponse<List<PublicationResponseDto>>> getPublicationsByFaculty(@PathVariable Long facultyId) {
        try {
            // Service method should return List<PublicationResponseDto>
            List<PublicationResponseDto> publications = publicationService.getPublicationsByFaculty(facultyId);
            return ResponseEntity.ok(ApiResponse.success("Publications retrieved successfully", publications));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve publications: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PublicationResponseDto>>> searchPublications(@RequestParam String keyword) {
        try {
            // Service method should return List<PublicationResponseDto>
            List<PublicationResponseDto> publications = publicationService.searchPublications(keyword);
            return ResponseEntity.ok(ApiResponse.success("Publications retrieved successfully", publications));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to search publications: " + e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<PublicationResponseDto>> createPublication(@Valid @RequestBody PublicationRequest request) {
        try {
            // Service method returns PublicationResponseDto after creation
            PublicationResponseDto createdPublication = publicationService.createPublication(request);
            return ResponseEntity.ok(ApiResponse.success("Publication created successfully", createdPublication));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create publication: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<PublicationResponseDto>> updatePublication(@PathVariable Long id,
                                                                     @Valid @RequestBody PublicationRequest request) {
        try {
            // Service method returns PublicationResponseDto after update
            PublicationResponseDto updatedPublication = publicationService.updatePublication(id, request);
            return ResponseEntity.ok(ApiResponse.success("Publication updated successfully", updatedPublication));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update publication: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<Void>> deletePublication(@PathVariable Long id) {
        try {
            publicationService.deletePublication(id);
            return ResponseEntity.ok(ApiResponse.success("Publication deleted successfully", null));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete publication: " + e.getMessage()));
        }
    }
}