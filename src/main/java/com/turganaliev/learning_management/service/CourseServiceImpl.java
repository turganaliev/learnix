package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.CourseResponseDto;
import com.turganaliev.learning_management.dto.SectionResponseDto;
import com.turganaliev.learning_management.exception.CourseNotFoundException;
import com.turganaliev.learning_management.model.Course;
import com.turganaliev.learning_management.model.Section;
import com.turganaliev.learning_management.repository.CourseRepository;
import com.turganaliev.learning_management.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Override
    public List<CourseResponseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new CourseResponseDto(course.getId(), course.getTitle(), course.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SectionResponseDto> getCourseSections(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found!"));

        List<Section> sections = sectionRepository.findByCourseOrderByOrderIndexAsc(course);
        return sections.stream()
                .map(section -> new SectionResponseDto(section.getId(), section.getContent(), section.getOrderIndex()))
                .collect(Collectors.toList());
    }
}
