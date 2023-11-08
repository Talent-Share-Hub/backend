package com.kangui.talentsharehub.domain.homework.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeworkAttachmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_attachment_file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homework_id")
    private Homework homework;

    private String uploadFileName; // 과제 업로드 파일 이름

    private String storeFileName; // 과제 저장 파일 이름

    private String fileUrl; // 과제 파일 접근 URL

    @Builder
    public HomeworkAttachmentFile(Homework homework, String uploadFileName, String storeFileName, String fileUrl) {
        this.homework = homework;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }

    public void changeHomework(Homework homework) {
        this.homework = homework;
    }
}
