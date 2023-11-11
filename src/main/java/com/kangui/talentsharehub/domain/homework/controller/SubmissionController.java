package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.dto.request.CreateSubmissionForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestSubmission;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseSubmission;
import com.kangui.talentsharehub.domain.homework.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "과제 제출 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "과제 제출 조회", description = "course-id에 해당하는 과제 제출 조회")
    @GetMapping
    public ResponseEntity<List<ResponseSubmission>> getSubmissionsByCourseId(@RequestParam("course-id") Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(submissionService.getSubmissionsByCourseId(courseId));
    }

    @Operation(summary = "과제 제출 생성", description = "과제 제출 생성")
    @PostMapping
    public ResponseEntity<Long> createSubmission(@Valid @ModelAttribute CreateSubmissionForm createSubmissionForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionService.createSubmission(createSubmissionForm));
    }

    @Operation(summary = "과제 제출 수정", description = "submission-id에 해당하는 과제 제출 수정")
    @PutMapping("/{submission-id}")
    public ResponseEntity<Long> updateSubmission(
            @PathVariable("submission-id") Long submissionId,
            @Valid @ModelAttribute RequestSubmission requestSubmission) {
        return ResponseEntity.status(HttpStatus.OK).body(submissionService.updateSubmission(submissionId, requestSubmission));
    }

    @Operation(summary = "과제 제출 삭제", description = "submission-id에 해당하는 과제 제출 삭제")
    @DeleteMapping("/{submission-id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable("submission-id") Long submissionId) {
        submissionService.deleteSubmission(submissionId);

        return ResponseEntity.noContent().build();
    }

}
