package com.kangui.talentsharehub.domain.course.entity;

import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Users user; // 선생님 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리 ID

    private String image_url; // 강의 사진 URL

    private String title; // 강의명

    @Lob
    private String description; // 강의 설명

    private String reference; // 참고자료 링크

    private String link; // 강의 링크

    private String contact; // 연락처

    private int capacity; // 수용인원

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus; // 강의 상태

    @Embedded
    private DateRange dateRange; // 시작일, 종료일

    @Builder
    public Course(Long id, Users user, Category category, String image_url, String title, String description, String reference, String link, String contact, int capacity, DateRange dateRange, CourseStatus courseStatus) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.image_url = image_url;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.link = link;
        this.contact = contact;
        this.capacity = capacity;
        this.dateRange = dateRange;
        this.courseStatus = courseStatus;
    }
}
