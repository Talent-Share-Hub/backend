package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.dto.request.CreateHomeworkForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseHomework;
import com.kangui.talentsharehub.domain.homework.service.HomeworkService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "과제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{course-id}/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "과제 조회", description = "course-id에 해당하는 과제 조회")
    @GetMapping
    public ResponseEntity<List<ResponseHomework>> getHomeworksByCourseId(
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(OK).body(homeworkService.getHomeworksByCourseId(principal, courseId));
    }

    @Operation(summary = "과제 생성", description = "과제 생성")
    @PostMapping
    public ResponseEntity<Long> createHomework(
            @Valid @ModelAttribute final CreateHomeworkForm requestCreateHomework,
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(CREATED)
                                .body(homeworkService.createHomework(requestCreateHomework, principal, courseId));
    }

    @Operation(summary = "과제 수정", description = "homework-id에 해당하는 과제 수정")
    @PutMapping("/{homework-id}")
    public ResponseEntity<Long> updateHomework(
            @Valid @ModelAttribute final RequestUpdateHomework requestUpdateHomework,
            @AuthPrincipal final Principal principal,
            @PathVariable("homework-id") final Long homeworkId
    ) {
        return ResponseEntity.status(OK).body(homeworkService.updateHomework(
                requestUpdateHomework, principal, homeworkId));
    }

    @Operation(summary = "과제 삭제", description = "homework-id에 해당하는 과제 삭제")
    @DeleteMapping("/{homework-id}")
    public ResponseEntity<Void> deleteHomework(
            @AuthPrincipal final Principal principal,
            @PathVariable("homework-id") final Long homeworkId
    ) {
        homeworkService.deleteHomework(principal, homeworkId);
        return ResponseEntity.noContent().build();
    }
}
