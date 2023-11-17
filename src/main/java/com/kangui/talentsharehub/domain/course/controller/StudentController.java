package com.kangui.talentsharehub.domain.course.controller;

import com.kangui.talentsharehub.domain.course.dto.response.ResponseStudentByCourseId;
import com.kangui.talentsharehub.domain.course.service.StudentService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "강의 수강생 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "강의 수강생 조회", description = "course-id에 해당하는 강의 수강생 조회")
    @GetMapping("/course/{course-id}")
    public ResponseEntity<List<ResponseStudentByCourseId>> getStudentsByCourseId(
            @PathVariable("course-id") final Long courseId)
    {
        return ResponseEntity.status(OK).body(studentService.getStudentsByCourseId(courseId));
    }

    @Operation(summary = "강의 수강생 등록", description = "강의 수강생 등록")
    @PostMapping("/enroll/course/{course-id}")
    public ResponseEntity<Long> enrollStudent(
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(CREATED).body(studentService.enrollStudent(principal, courseId));
    }

    @Operation(summary = "강의 수강생 탈퇴", description = "강의 수강생 탈퇴")
    @DeleteMapping("/course/{course-id}")
    public ResponseEntity<Void> removeStudentFromCourse(
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        studentService.removeStudentFromCourse(principal, courseId);
        return ResponseEntity.noContent().build();
    }
}
