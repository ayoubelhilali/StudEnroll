package com.demo.enrollment_service.dto;

public class EnrollmentResponseDTO {
	private Long enrollmentId;
    private String studentCnie;
    private String courseName;
    private String date;
    private boolean deletable;

    // Getters and Setters
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getStudentCnie() { return studentCnie; }
    public void setStudentCnie(String studentCnie) { this.studentCnie = studentCnie; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isDeletable() { return deletable; }
    public void setDeletable(boolean deletable) { this.deletable = deletable; }
}
