package com.kangui.talentsharehub.domain.course.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_image_file_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    private String uploadFileName; // 강의 이미지 업로드 파일 이름

    private String storeFileName; // 강의 이미지 저장 파일 이름

    private String fileUrl; // 강의 이미지 접근 url

    @Builder
    public CourseImageFile(String uploadFileName, String storeFileName, String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }

    public void updateImageFile(String uploadFileName, String storeFileName, String fileUrl) {
        if (StringUtils.hasText(uploadFileName)) {
            this.uploadFileName= uploadFileName;
        }

        if (StringUtils.hasText(storeFileName)) {
            this.storeFileName= storeFileName;
        }

        if (StringUtils.hasText(fileUrl)) {
            this.fileUrl= fileUrl;
        }
    }
}
