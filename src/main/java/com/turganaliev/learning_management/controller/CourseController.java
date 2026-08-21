package com.turganaliev.learning_management.controller;

import com.turganaliev.learning_management.dto.CourseResponseDto;
import com.turganaliev.learning_management.dto.SectionResponseDto;
import com.turganaliev.learning_management.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> courses() {
        List<CourseResponseDto> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    @GetMapping("/{courseId}/sections")
    public ResponseEntity<?> sections(@PathVariable Long courseId) {
        List<SectionResponseDto> courseSections = courseService.getCourseSections(courseId);
        return ResponseEntity.ok(courseSections);
    }
}
