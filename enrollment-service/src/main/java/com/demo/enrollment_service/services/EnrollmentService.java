package com.demo.enrollment_service.services;

import com.demo.enrollment_service.clients.CourseClient;
import com.demo.enrollment_service.clients.StudentClient;
import com.demo.enrollment_service.dto.EnrollmentRequest;
import com.demo.enrollment_service.dto.EnrollmentResponseDTO;
import com.demo.enrollment_service.entities.Enrollment;
import com.demo.enrollment_service.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentClient studentClient; // Using the new client

    @Autowired
    private CourseClient courseClient;   // Using the new client

    public Mono<Enrollment> processEnrollment(EnrollmentRequest request) {
        
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentCnieAndCourseId(
                request.getStudentCnie(), request.getCourseId()
        );
        
        if (alreadyEnrolled) {
            return Mono.error(new IllegalStateException("Enrollment failed: Student is already enrolled in this course."));
        }

        long currentStudents = enrollmentRepository.countByCourseId(request.getCourseId());
        if (currentStudents >= 3) {
            return Mono.error(new IllegalStateException("Enrollment failed: Course is at maximum capacity (3 students)."));
        }

        // Use the dedicated clients to fetch data
        return Mono.zip(
                studentClient.getStudentByCnie(request.getStudentCnie()),
                courseClient.getCourseById(request.getCourseId())
        ).map(tuple -> {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentCnie(request.getStudentCnie());
            enrollment.setCourseId(request.getCourseId());
            return enrollmentRepository.save(enrollment);
        });
    }

    public Flux<EnrollmentResponseDTO> getDashboardEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();

        return Flux.fromIterable(enrollments)
                .flatMap(enrollment -> courseClient.getCourseById(enrollment.getCourseId())
                        .map(course -> {
                            EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
                            dto.setEnrollmentId(enrollment.getId());
                            dto.setStudentCnie(enrollment.getStudentCnie());
                            dto.setCourseName(course.getTitle());
                            dto.setDate(enrollment.getEnrollmentDate().toString());

                            boolean isDeletable = enrollment.getEnrollmentDate()
                                    .isAfter(LocalDateTime.now().minusHours(24));
                            dto.setDeletable(isDeletable);

                            return dto;
                        })
                );
    }

    public Mono<Void> cancelEnrollment(Long enrollmentId) {
        return Mono.justOrEmpty(enrollmentRepository.findById(enrollmentId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Enrollment not found.")))
                .flatMap(enrollment -> {
                    boolean isDeletable = enrollment.getEnrollmentDate()
                            .isAfter(LocalDateTime.now().minusHours(24));
                    
                    if (!isDeletable) {
                        return Mono.error(new IllegalStateException("Enrollment failed: Cannot cancel an enrollment older than 24 hours."));
                    }
                    
                    enrollmentRepository.delete(enrollment);
                    return Mono.empty();
                });
    }
}