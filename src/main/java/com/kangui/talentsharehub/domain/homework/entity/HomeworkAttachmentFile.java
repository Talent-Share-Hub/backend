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

    private String fileName;

    private String filePath;

    @Builder
    public HomeworkAttachmentFile(Homework homework, String fileName, String filePath) {
        this.homework = homework;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void changeHomework(Homework homework) {
        this.homework = homework;
    }
}
