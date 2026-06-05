package com.demo.enrollment_service.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentCnie;
    private String courseId; // String to match MongoDB's ID
    private LocalDateTime enrollmentDate;

    public Enrollment() {
        this.enrollmentDate = LocalDateTime.now();
    }

    public Enrollment(String studentCnie, String courseId) {
        this.studentCnie = studentCnie;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentCnie() { return studentCnie; }
    public void setStudentCnie(String studentCnie) { this.studentCnie = studentCnie; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}