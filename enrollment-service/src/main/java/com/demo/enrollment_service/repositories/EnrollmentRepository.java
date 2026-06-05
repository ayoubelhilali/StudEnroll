package com.demo.enrollment_service.repositories;

import com.demo.enrollment_service.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	// Count enrollments by course
	long countByCourseId(String courseId);
	// Check for existing enrollments
	boolean existsByStudentCnieAndCourseId(String studentCnie, String courseId);
}