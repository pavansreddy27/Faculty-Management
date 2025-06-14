package model;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class Faculty {
    @Id
    private String id;

    private String name;
    private String dob;
    private String phone;
    private String email;
    private String address;
    private String qualification;
    private int experience;
    private String designation;
    private String subjects;

    @Lob
    private byte[] photo;
}
