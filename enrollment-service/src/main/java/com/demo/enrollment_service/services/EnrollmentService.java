package com.demo.enrollment_service.services;

import com.demo.enrollment_service.dto.CourseDTO;

import com.demo.enrollment_service.dto.StudentDTO;
import com.demo.enrollment_service.entities.Enrollment;
import com.demo.enrollment_service.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.enrollment_service.dto.EnrollmentResponseDTO;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;
import java.util.List;
import reactor.core.publisher.Mono;

@Service
public class EnrollmentService {
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public Mono<Enrollment> processEnrollment(Enrollment enrollment){
		// Check for duplicate enrollment
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentCnieAndCourseId(
                enrollment.getStudentCnie(), enrollment.getCourseId()
        );
        
        if (alreadyEnrolled) {
            return Mono.error(new IllegalStateException("Enrollment failed: Student is already enrolled in this course."));
        }
		// Check Capacity
		long currentStudents = enrollmentRepository.countByCourseId(enrollment.getCourseId());
		
		if(currentStudents >= 3) {
			return Mono.error(new IllegalStateException("Enrollment failed: course is at maximum capacity"));
		}
		
		// Fetch Student
		Mono<StudentDTO> studentReq = webClientBuilder.build().get()
                .uri("http://STUDENT-SERVICE/api/students/cnie/" + enrollment.getStudentCnie())
                .retrieve()
                .bodyToMono(StudentDTO.class);
		
		// Fetch Course
        Mono<CourseDTO> courseReq = webClientBuilder.build().get()
                .uri("http://COURSE-SERVICE/api/courses/" + enrollment.getCourseId())
                .retrieve()
                .bodyToMono(CourseDTO.class);

        // If everything is good, save to MySQL!
        return Mono.zip(studentReq, courseReq)
                .map(tuple -> enrollmentRepository.save(enrollment));
	}
	
	
	public Flux<EnrollmentResponseDTO> getDashboardEnrollments() {
        // 1. Get all raw enrollments from MySQL
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        // 2. Turn them into a reactive stream
        return Flux.fromIterable(enrollments)
                .flatMap(enrollment -> {
                    // 3. Ask Course Service for the Course Title
                    return webClientBuilder.build().get()
                            .uri("http://COURSE-SERVICE/api/courses/" + enrollment.getCourseId())
                            .retrieve()
                            .bodyToMono(CourseDTO.class)
                            .map(course -> {
                                // 4. Build the final DTO for the frontend
                                EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
                                dto.setEnrollmentId(enrollment.getId());
                                dto.setStudentCnie(enrollment.getStudentCnie());
                                dto.setCourseName(course.getTitle()); 
                                dto.setDate(enrollment.getEnrollmentDate().toString());

                                // 5. The 24-Hour Rule! 
                                // True if the enrollment date is AFTER (now minus 24 hours)
                                boolean isDeletable = enrollment.getEnrollmentDate()
                                        .isAfter(LocalDateTime.now().minusHours(24));
                                dto.setDeletable(isDeletable);

                                return dto;
                            });
                });
    }

}
