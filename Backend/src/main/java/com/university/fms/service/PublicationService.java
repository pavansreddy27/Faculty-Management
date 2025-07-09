package com.university.fms.service;

import com.university.fms.dto.request.PublicationRequest;
import com.university.fms.dto.response.PublicationResponseDto;
import com.university.fms.entity.Publication; // Your JPA Entity
import com.university.fms.repository.PublicationRepository; // Your Spring Data JPA Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    // Helper method to convert Entity to DTO
    private PublicationResponseDto convertToDto(Publication publication) {
        PublicationResponseDto dto = new PublicationResponseDto();
        dto.setId(publication.getId());
        dto.setTitle(publication.getTitle()); // Ensure your Publication entity has a getTitle()
        dto.setPublicationDate(publication.getPublicationDate());
        dto.setJournalName(publication.getJournalName());
        dto.setUrl(publication.getUrl());
        dto.setAbstractText(publication.getAbstractText());
        dto.setDoi(publication.getDoi());
        // Assuming facultyId is accessible from Publication entity or its relationship
        // If Publication entity has a Faculty relationship, you might need to fetch faculty ID like:
        // if (publication.getFaculty() != null) {
        //     dto.setFacultyId(publication.getFaculty().getId());
        // }
        // For simplicity, let's assume it's directly available or fetched.
        // You'll need to adjust based on your actual Publication entity structure.
        // For now, if facultyId is part of the Publication entity, use:
        // dto.setFacultyId(publication.getFacultyId()); // If directly a field
        return dto;
    }

    // Helper method to convert Request DTO to Entity
    private Publication convertToEntity(PublicationRequest request) {
        Publication publication = new Publication();
        // ID is usually set by DB on creation, or for updates, it comes from path variable
        publication.setTitle(request.getTitle());
        publication.setPublicationDate(request.getPublicationDate());
        publication.setJournalName(request.getJournalName());
        publication.setUrl(request.getUrl());
        publication.setAbstractText(request.getAbstractText());
        publication.setDoi(request.getDoi());
        // Handle facultyId. This might involve looking up a Faculty entity if it's a relationship.
        // For simplicity, directly setting it, but adjust based on your actual entity model.
        // publication.setFacultyId(request.getFacultyId());
        return publication;
    }

    public List<PublicationResponseDto> getAllPublications() {
        return publicationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PublicationResponseDto> getPublicationById(Long id) {
        return publicationRepository.findById(id)
                .map(this::convertToDto); // Convert entity to DTO if found
    }

    public List<PublicationResponseDto> getPublicationsByFaculty(Long facultyId) {
        // You'll need a method in your repository, e.g., findByFacultyId
        // Or if Publication has a relationship to Faculty, find publications associated with that faculty
        // Example: List<Publication> publications = publicationRepository.findByFaculty_Id(facultyId);
        List<Publication> publications = publicationRepository.findAll(); // Placeholder, implement actual logic
        return publications.stream()
                .filter(p -> {
                    // Replace with actual logic to check facultyId on Publication entity
                    // e.g., p.getFaculty() != null && p.getFaculty().getId().equals(facultyId)
                    return true; // Placeholder
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PublicationResponseDto> searchPublications(String keyword) {
        // Implement your search logic in the repository or service
        // Example: List<Publication> publications = publicationRepository.findByTitleContainingIgnoreCaseOrAbstractTextContainingIgnoreCase(keyword, keyword);
        List<Publication> publications = publicationRepository.findAll(); // Placeholder
        return publications.stream()
                .filter(p -> p.getTitle().toLowerCase().contains(keyword.toLowerCase())) // Simple title search for example
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PublicationResponseDto createPublication(PublicationRequest request) {
        Publication publication = convertToEntity(request);
        Publication savedPublication = publicationRepository.save(publication);
        return convertToDto(savedPublication);
    }

    public PublicationResponseDto updatePublication(Long id, PublicationRequest request) {
        return publicationRepository.findById(id)
                .map(existingPublication -> {
                    // Update fields from request
                    existingPublication.setTitle(request.getTitle());
                    existingPublication.setPublicationDate(request.getPublicationDate());
                    existingPublication.setJournalName(request.getJournalName());
                    existingPublication.setUrl(request.getUrl());
                    existingPublication.setAbstractText(request.getAbstractText());
                    existingPublication.setDoi(request.getDoi());
                    // Don't update facultyId from request on update unless specifically intended
                    // existingPublication.setFacultyId(request.getFacultyId()); // Be cautious with this if faculty is a fixed relationship

                    Publication updatedPublication = publicationRepository.save(existingPublication);
                    return convertToDto(updatedPublication);
                })
                .orElseThrow(() -> new RuntimeException("Publication not found with ID: " + id)); // Or custom exception
    }

    public void deletePublication(Long id) {
        publicationRepository.deleteById(id);
    }
}