package com.demo.course_service.repositories;

import com.demo.course_service.entities.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> { 
	// Changed to MongoRepository and String
}