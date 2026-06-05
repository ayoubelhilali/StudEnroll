package com.demo.student_service.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cnie;

    private String firstName;
    
    private String lastName;

    private String email;

    // Constructors
    public Student() {}

    public Student(String cnie, String firstName, String lastName, String email) {
        this.cnie = cnie;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters (Important for Spring Data JPA and JSON serialization)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCnie() { return cnie; }
    public void setCnie(String cnie) { this.cnie = cnie; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}