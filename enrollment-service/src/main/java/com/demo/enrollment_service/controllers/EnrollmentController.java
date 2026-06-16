package com.demo.enrollment_service.controllers;

import com.demo.enrollment_service.dto.CourseDTO;
import com.demo.enrollment_service.dto.EnrollmentRequest;
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
    public Mono<ResponseEntity<Object>> enrollStudent(@RequestBody EnrollmentRequest request) {
        
        return enrollmentService.processEnrollment(request)
                .map(savedEnrollment -> ResponseEntity.status(HttpStatus.CREATED).body((Object) savedEnrollment))
                .onErrorResume(IllegalStateException.class, e -> 
                    Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())))
                .onErrorResume(Exception.class, e ->
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred: " + e.getMessage()))
                );
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> cancelEnrollment(@PathVariable Long id) {
        return enrollmentService.cancelEnrollment(id)
                // If successful, return 204 No Content
                .then(Mono.just(ResponseEntity.noContent().build()))
                
                // If past 24 hours, return 400 Bad Request
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())))
                
                // If ID doesn't exist, return 404 Not Found
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage())));
    }
}