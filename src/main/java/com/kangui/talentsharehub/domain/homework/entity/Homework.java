package com.kangui.talentsharehub.domain.homework.entity;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course; // 강의 ID

    private String title; // 제목

    @Lob
    private String contents; // 숙제 내용

    private String referenceFile; // 참고 파일

    private LocalDateTime endDate; // 마감 일자

    @Builder
    public Homework(Long id, Course course, String title, String contents, String referenceFile, LocalDateTime endDate) {
        this.id = id;
        this.course = course;
        this.title = title;
        this.contents = contents;
        this.referenceFile = referenceFile;
        this.endDate = endDate;
    }
}
