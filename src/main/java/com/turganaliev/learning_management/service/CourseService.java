package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.CourseResponseDto;
import com.turganaliev.learning_management.dto.SectionResponseDto;

import java.util.List;

public interface CourseService {
    List<CourseResponseDto> getAllCourses();
    List<SectionResponseDto> getCourseSections(Long courseId);
}
