package com.turganaliev.learning_management.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Entity
@Table(name = "sections")
@Data

public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;
}
