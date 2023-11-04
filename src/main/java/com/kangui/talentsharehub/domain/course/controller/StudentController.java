package com.kangui.talentsharehub.domain.course.controller;

import com.kangui.talentsharehub.domain.course.dto.request.RequestEnrollStudent;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseStudentByCourseId;
import com.kangui.talentsharehub.domain.course.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "강의 수강생 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "강의 수강생 등록", description = "강의 수강생 등록")
    @PostMapping("/enroll")
    public ResponseEntity<Long> enrollStudent(@RequestBody RequestEnrollStudent requestEnrollStudent) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.enrollStudent(requestEnrollStudent));
    }

    @Operation(summary = "강의 수강생 조회", description = "course-id에 해당하는 강의 수강생 조회")
    @GetMapping("/course/{course-id}")
    public ResponseEntity<List<ResponseStudentByCourseId>> getStudentsByCourseId(@PathVariable("course-id") Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByCourseId(courseId));
    }

    // 강의 수강생 삭제 API
    @DeleteMapping("/course/{courseId}/user/{userId}")
    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        studentService.removeStudentFromCourse(courseId, userId);
        return ResponseEntity.noContent().build();
    }
}
