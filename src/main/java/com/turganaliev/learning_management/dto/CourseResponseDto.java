package com.turganaliev.learning_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String title;
    private String description;
}
