package com.university.fms.dto.response;

// No need for annotations like @JsonManagedReference/JsonBackReference here
// because DTOs represent a flat view for serialization

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    // private List<String> roles; // You might include roles here if needed for display

    // Constructor from User entity
    public UserResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // Setters (optional for response DTOs)
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
}