package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.service.HomeworkAttachmentFileService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "과제 첨부 파일 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{course-id}/homework-attachment-file")
public class HomeworkAttachmentFileController {

    private final HomeworkAttachmentFileService homeworkAttachmentFileService;

    @Operation(summary = "과제 첨부 파일 다운로드", description = "file-id에 해당하는 과제 첨부 파일 다운로드")
    @GetMapping("/{file-id}")
    public ResponseEntity<Resource> downloadAttachById(
            final HttpServletResponse response,
            @AuthPrincipal final Principal principal,
            @PathVariable("{file-id}") final Long fileId
    ) {
        return ResponseEntity.status(OK)
                .body(homeworkAttachmentFileService.downloadAttachById(response, principal, fileId));
    }

    @Operation(summary = "과제 첨부 파일 추가", description = "homework-id에 해당 하는 파일 추가")
    @PostMapping(value = "/homework/{homework-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> addAttachmentFileByHomeworkId(
            @AuthPrincipal final Principal principal,
            @PathVariable("homework-id") final Long homeworkId,
            @RequestParam("attachmentFile") final MultipartFile attachmentFile
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(homeworkAttachmentFileService
                        .addAttachmentFileByHomeworkId(principal, homeworkId, attachmentFile));
    }

    @Operation(summary = "과제 첨부 파일 삭제", description = "file-id에 해당하는 과제 첨부 파일 삭제")
    @DeleteMapping("/{file-id}")
    public ResponseEntity<Void> deleteAttachmentFileById(
            @AuthPrincipal final Principal principal,
            @PathVariable("file-id") final Long fileId
    ) {
        homeworkAttachmentFileService.deleteAttachmentFileById(principal, fileId);
        return ResponseEntity.noContent().build();
    }
}
