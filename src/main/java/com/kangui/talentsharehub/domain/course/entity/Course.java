package com.kangui.talentsharehub.domain.course.entity;

import com.kangui.talentsharehub.domain.course.dto.request.UpdateCourseForm;
import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
    private Users user; // 선생님 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category; // 카테고리 ID

    @OneToOne(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private CourseImageFile courseImageFile;

    private String title; // 강의명

    @Lob
    private String description; // 강의 설명

    private String reference; // 참고자료 링크

    private String link; // 강의 링크

    private String contact; // 연락처

    private int capacity; // 수용 인원

    private int enrolledStudents; // 등록 인원

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus; // 강의 상태

    @Embedded
    private DateRange dateRange; // 시작일, 종료일

    @Builder
    public Course(Long id, Users user, Category category, CourseImageFile courseImageFile, String title, String description, String reference, String link, String contact, int capacity, int enrolledStudents, CourseStatus courseStatus, DateRange dateRange) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.courseImageFile = courseImageFile;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.link = link;
        this.contact = contact;
        this.capacity = capacity;
        this.enrolledStudents = enrolledStudents;
        this.courseStatus = courseStatus;
        this.dateRange = dateRange;
    }

    public void updateCourse(UpdateCourseForm requestUpdateCourse) {
        this.title = StringUtils.hasText(requestUpdateCourse.getTitle()) ? requestUpdateCourse.getTitle() : this.title;
        this.description = StringUtils.hasText(requestUpdateCourse.getDescription()) ? requestUpdateCourse.getDescription() : this.description;
        this.reference = StringUtils.hasText(requestUpdateCourse.getReference()) ? requestUpdateCourse.getReference() : this.reference;
        this.link = StringUtils.hasText(requestUpdateCourse.getLink()) ? requestUpdateCourse.getLink() : this.link;
        this.contact = StringUtils.hasText(requestUpdateCourse.getContact()) ? requestUpdateCourse.getContact() : this.contact;
        this.capacity = requestUpdateCourse.getCapacity() > 0 ? requestUpdateCourse.getCapacity() : this.capacity;

        if (requestUpdateCourse.getStartDate() != null && requestUpdateCourse.getEndDate() != null && !requestUpdateCourse.getStartDate().isAfter(requestUpdateCourse.getEndDate())) {
            this.dateRange = DateRange.builder()
                    .startDate(requestUpdateCourse.getStartDate())
                    .endDate(requestUpdateCourse.getEndDate())
                    .build();
        }
    }
}
