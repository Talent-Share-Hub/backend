package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.service.SubmissionAttachmentFileService;
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

@Tag(name = "과제 제출 첨부 파일 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission-attachment-file")
public class SubmissionAttachmentFileController {

    private final SubmissionAttachmentFileService submissionAttachmentFileService;

    @Operation(summary = "과제 제출 첨부 파일 다운로드", description = "file-id에 해당하는 첨부 파일 다운로드")
    @GetMapping("/{file-id}")
    public ResponseEntity<Resource> downloadAttachById(
            final HttpServletResponse response,
            @AuthPrincipal final Principal principal,
            @PathVariable("file-id") final Long fileId
    ) {
        return ResponseEntity.status(OK)
                .body(submissionAttachmentFileService.downloadAttachById(response, principal, fileId));
    }

    @Operation(summary = "과제 제출 첨부 파일 추가", description = "submission-id에 해당 하는 파일 추가")
    @PostMapping(value = "/submission/{submission-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> addAttachmentFileBySubmissionId(
            @AuthPrincipal final Principal principal,
            @RequestParam("attachmentFile") final MultipartFile attachmentFile,
            @PathVariable("submission-id") final Long submissionId
    ){
        return ResponseEntity
                .status(CREATED)
                .body(submissionAttachmentFileService
                        .addAttachmentFileBySubmissionId(principal, attachmentFile, submissionId));

    }

    @Operation(summary = "과제 제출 첨부 파일 삭제", description = "file-id에 해당하는 과제 첨부 파일 삭제")
    @DeleteMapping("/{file-id}")
    public ResponseEntity<Void> deleteAttachmentFileById(
            @AuthPrincipal final Principal principal,
            @PathVariable("file-id") final Long fileId
    ) {
        submissionAttachmentFileService.deleteAttachmentFileById(principal, fileId);
        return ResponseEntity.noContent().build();
    }
}
