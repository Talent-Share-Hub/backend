package com.kangui.talentsharehub.domain.homework.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionAttachmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_attachment_file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    private String uploadFileName; // 과제 업로드 파일 이름

    private String storeFileName; // 과제 저장 파일 이름

    private String fileUrl; // 과제 파일 접근 URL

    public SubmissionAttachmentFile(Submission submission, String uploadFileName, String storeFileName, String fileUrl) {
        this.submission = submission;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }

    public void changeSubmission(Submission submission) {
        this.submission = submission;
    }
}
