package com.kangui.talentsharehub.domain.homework.entity;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HomeworkAttachmentFile> homeworkAttachmentFile = new ArrayList<>();

    private LocalDateTime startDate; // 시작 일자

    private LocalDateTime endDate; // 마감 일자

    @Builder
    public Homework(Course course, String title, String contents, List<HomeworkAttachmentFile> homeworkAttachmentFile, LocalDateTime startDate, LocalDateTime endDate) {
        this.course = course;
        this.title = title;
        this.contents = contents;
        this.homeworkAttachmentFile = homeworkAttachmentFile;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addHomeworkAttachmentFile(HomeworkAttachmentFile attachmentFile) {
        this.homeworkAttachmentFile.add(attachmentFile);
        attachmentFile.changeHomework(this);
    }

    public void updateHomework(String title, String contents, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.contents = contents;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
