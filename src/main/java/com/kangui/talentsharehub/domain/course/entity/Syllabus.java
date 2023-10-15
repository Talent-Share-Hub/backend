package com.kangui.talentsharehub.domain.course.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "syllabus_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course; // 강의 ID

    private int week; // 주차

    private String courseContent; // 강의 내용

    @Builder
    public Syllabus(Long id, Course course, int week, String courseContent) {
        this.id = id;
        this.course = course;
        this.week = week;
        this.courseContent = courseContent;
    }
}
