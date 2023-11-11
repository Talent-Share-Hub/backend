package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.service.HomeworkAttachmentFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "과제 첨부 파일 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/homework-attachment-file")
public class HomeworkAttachmentFileController {

    private final HomeworkAttachmentFileService homeworkAttachmentFileService;

    @Operation(summary = "과제 첨부 파일 다운로드", description = "file-id에 해당하는 과제 첨부 파일 다운로드")
    @GetMapping("/{file-id}")
    public ResponseEntity<Resource> downloadAttachById(HttpServletResponse response, @PathVariable Long fileId) {
        return ResponseEntity.status(HttpStatus.OK).body(homeworkAttachmentFileService.downloadAttachById(response, fileId));
    }

    @Operation(summary = "과제 첨부 파일 추가", description = "homework-id에 해당 하는 파일 추가")
    @PostMapping("/homework/{homework-id}")
    public ResponseEntity<Long> addAttachmentFileByHomeworkId(
            @PathVariable("homework-id") Long homeworkId,
            @RequestParam("attachmentFile") MultipartFile attachmentFile) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(homeworkAttachmentFileService.addAttachmentFileByHomeworkId(homeworkId, attachmentFile));
    }

    @Operation(summary = "과제 첨부 파일 삭제", description = "file-id에 해당하는 과제 첨부 파일 삭제")
    @DeleteMapping("/{file-id}")
    public ResponseEntity<Void> deleteAttachmentFileById(@PathVariable("file-id") Long fileId) {
        homeworkAttachmentFileService.deleteAttachmentFileById(fileId);
        return ResponseEntity.noContent().build();
    }
}
