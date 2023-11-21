package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.dto.request.CreateSubmissionForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateSubmission;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseSubmission;
import com.kangui.talentsharehub.domain.homework.service.SubmissionService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "과제 제출 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "본인 제출 과제 조회", description = "submission-id에 해당 하는 나의 제출 과제 조회")
    @GetMapping("/{submission-id}")
    public ResponseEntity<ResponseSubmission> getSubmissionsByCourseId(
            @AuthPrincipal final Principal principal,
            @PathVariable("submission-id") final Long submissionId
    ) {
        return ResponseEntity.status(OK).body(submissionService.getSubmissionsById(principal, submissionId));
    }

    @Operation(summary = "과제 제출 생성", description = "과제 제출 생성")
    @PostMapping(value = "/course/{course-id}/homework/{homework-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createSubmission(
            @AuthPrincipal Principal principal,
            @Valid @ModelAttribute final CreateSubmissionForm createSubmissionForm,
            @PathVariable("course-id") final Long courseId,
            @PathVariable("homework-id") final Long homeworkId
    ) {
        return ResponseEntity.status(CREATED).body(submissionService.createSubmission(principal, createSubmissionForm, courseId, homeworkId));
    }

    @Operation(summary = "과제 제출 수정", description = "submission-id에 해당하는 과제 제출 수정")
    @PutMapping("/{submission-id}")
    public ResponseEntity<Long> updateSubmission(
            @AuthPrincipal Principal principal,
            @PathVariable("submission-id") Long submissionId,
            @Valid @RequestBody RequestUpdateSubmission requestSubmission
    ) {
        return ResponseEntity.status(OK).body(submissionService.updateSubmission(principal, submissionId, requestSubmission));
    }

    @Operation(summary = "과제 제출 삭제", description = "submission-id에 해당하는 과제 제출 삭제")
    @DeleteMapping("/{submission-id}")
    public ResponseEntity<Void> deleteSubmission(
            @AuthPrincipal Principal principal,
            @PathVariable("submission-id") final Long submissionId
    ) {
        submissionService.deleteSubmission(principal, submissionId);
        return ResponseEntity.noContent().build();
    }

}
