package com.demo.student_service.controllers;

import com.demo.student_service.entities.Student;
import com.demo.student_service.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Optional: Add a simple GET to retrieve all students to verify data
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Required: GET /api/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Required: GET /api/students/cnie/{cnie} (Crucial for validation logic later)
    @GetMapping("/cnie/{cnie}")
    public ResponseEntity<Student> getStudentByCnie(@PathVariable String cnie) {
        Optional<Student> student = studentRepository.findByCnie(cnie);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    // Optional: A POST endpoint to easily add test data
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }
}