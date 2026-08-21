package com.turganaliev.learning_management.repository;

import com.turganaliev.learning_management.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, Long> {
}
