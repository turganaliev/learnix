package com.turganaliev.learning_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SectionResponseDto {
    private Long id;
    private String content;
    private Integer orderIndex;
}
