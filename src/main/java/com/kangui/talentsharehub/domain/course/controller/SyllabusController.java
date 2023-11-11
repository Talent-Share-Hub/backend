package com.kangui.talentsharehub.domain.course.controller;

import com.kangui.talentsharehub.domain.course.dto.request.RequestCreateSyllabus;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseSyllabusByCourseId;
import com.kangui.talentsharehub.domain.course.service.SyllabusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "강의 계획서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/syllabus")
public class SyllabusController {

    private final SyllabusService syllabusService;

    @Operation(summary = "강의 계획서 조회", description = "course-id에 해당하는 강의 계획서 조회")
    @GetMapping
    public ResponseEntity<List<ResponseSyllabusByCourseId>> getSyllabusByCourseId(@RequestParam("course-id") Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(syllabusService.getSyllabusByCourseId(courseId));
    }

    @Operation(summary = "강의 계획서 생성", description = "강의 계획서 생성")
    @PostMapping
    public ResponseEntity<Long> createSyllabus(@Valid @RequestBody RequestCreateSyllabus requestCreateSyllabus) {
        return ResponseEntity.status(HttpStatus.CREATED).body(syllabusService.createSyllabus(requestCreateSyllabus));
    }

}
