package com.kangui.talentsharehub.domain.homework.entity;

import com.kangui.talentsharehub.domain.course.entity.Student;
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
@Table(name = "homework_submission")
public class Submission extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_student_id")
    private Student student; // 수강생 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_id")
    private Homework homework; // 과제 ID

    private String contents; // 제출 코멘트

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionAttachmentFile> submissionAttachmentFile = new ArrayList<>();

    private LocalDateTime submissionDate; // 제출 일자

    @Builder
    public Submission(Long id, Student student, Homework homework, String contents, List<SubmissionAttachmentFile> submissionAttachmentFile, LocalDateTime submissionDate) {
        this.id = id;
        this.student = student;
        this.homework = homework;
        this.contents = contents;
        this.submissionAttachmentFile = submissionAttachmentFile;
        this.submissionDate = submissionDate;
    }
}
