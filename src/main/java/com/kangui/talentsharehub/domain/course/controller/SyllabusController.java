package com.kangui.talentsharehub.domain.course.controller;

import com.kangui.talentsharehub.domain.course.dto.request.RequestCreateSyllabus;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseSyllabusByCourseId;
import com.kangui.talentsharehub.domain.course.service.SyllabusService;
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

@Tag(name = "강의 계획서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/syllabus")
public class SyllabusController {

    private final SyllabusService syllabusService;

    @Operation(summary = "강의 계획서 조회", description = "course-id에 해당하는 강의 계획서 조회")
    @GetMapping
    public ResponseEntity<List<ResponseSyllabusByCourseId>> getSyllabusByCourseId(
            @RequestParam("course-id") final Long courseId)
    {
        return ResponseEntity.status(OK).body(syllabusService.getSyllabusByCourseId(courseId));
    }

    @Operation(summary = "강의 계획서 생성", description = "강의 계획서 생성")
    @PostMapping("/course/{course-id}")
    public ResponseEntity<Long> createSyllabus(
            @Valid @RequestBody final RequestCreateSyllabus requestCreateSyllabus,
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(CREATED).body(syllabusService.createSyllabus(requestCreateSyllabus, principal, courseId));
    }

}
