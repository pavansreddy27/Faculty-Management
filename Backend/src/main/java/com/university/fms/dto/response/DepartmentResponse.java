package com.university.fms.dto.response;

public class DepartmentResponse {
    private Long id;
    private String name;
    // You can include other department fields here

    public DepartmentResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }

    // Setters (optional)
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}