package com.kangui.talentsharehub.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_file_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    private String uploadFileName; // 프로필 이미지 업로드 파일 이름

    private String storeFileName; // 프로필 이미지 저장 파일 이름

    private String fileUrl; // 프로필 이미지 접근 URL

    @Builder
    public UserImageFile(Users user, String uploadFileName, String storeFileName, String fileUrl) {
        this.user = user;
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
