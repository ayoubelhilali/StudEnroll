package com.demo.enrollment_service.controllers;

import com.demo.enrollment_service.dto.CourseDTO;
import com.demo.enrollment_service.dto.EnrollmentResponseDTO;
import com.demo.enrollment_service.dto.StudentDTO;
import com.demo.enrollment_service.entities.Enrollment;
import com.demo.enrollment_service.repositories.EnrollmentRepository;
import com.demo.enrollment_service.services.EnrollmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono; // Important new import!

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public Flux<EnrollmentResponseDTO> getAllEnrollments() {
        return enrollmentService.getDashboardEnrollments();
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> enrollStudent(@RequestBody Enrollment enrollment) {
        return enrollmentService.processEnrollment(enrollment)
        		// Success
        		.map(savedEnrollment -> ResponseEntity.status(HttpStatus.CREATED).body((Object) savedEnrollment))
        		// Error: Capacity is full
        		.onErrorResume(IllegalStateException.class, e -> 
        				Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())))
        		// If Student or Course is missing, catch the 404
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Enrollment failed: Student CNIE or Course ID does not exist."));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error communicating with other services."));
                })
                
                // Catch any other unexpected crashes
                .onErrorResume(Exception.class, e ->
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An unexpected error occurred: " + e.getMessage()))
                );
    }
}