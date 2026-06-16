package com.demo.enrollment_service.dto;

public class EnrollmentRequest {
    private String studentCnie;
    private String courseId;

    // Getters and Setters
    public String getStudentCnie() { return studentCnie; }
    public void setStudentCnie(String studentCnie) { this.studentCnie = studentCnie; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
}