package com.demo.student_service.repositories;


import com.demo.student_service.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Custom query to find a student by their unique CNIE code
    Optional<Student> findByCnie(String cnie);
}
