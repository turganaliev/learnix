package com.turganaliev.learning_management.repository;

import com.turganaliev.learning_management.model.Course;
import com.turganaliev.learning_management.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseOrderByOrderIndexAsc(Course course);
}
