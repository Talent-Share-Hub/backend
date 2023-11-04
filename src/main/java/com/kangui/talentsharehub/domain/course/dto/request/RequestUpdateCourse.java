package com.kangui.talentsharehub.domain.course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class RequestUpdateCourse {

    @Schema(description = "강의 메인 이미지")
    private MultipartFile courseImage;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "참고 자료 링크")
    private String reference;

    @Schema(description = "강의 링크")
    private String link;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "수용 인원")
    private int capacity;

    @Schema(description = "강의 시작일")
    private LocalDate startDate;

    @Schema(description = "강의 종료일")
    private LocalDate endDate;

}
