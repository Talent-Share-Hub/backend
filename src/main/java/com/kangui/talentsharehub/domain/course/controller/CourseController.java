package com.kangui.talentsharehub.domain.course.controller;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.request.CreateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.request.UpdateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCourseById;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import com.kangui.talentsharehub.domain.course.service.CourseService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "강의 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "모임 조회", description = "모임 조회 (검색, 카테고리별 필터링 가능, 페이지네이션)")
    @Parameters({
            @Parameter(name = "search", description = "검색", example = "AWS 강의", required = false),
            @Parameter(name = "category", description = "카테고리", example = "프로그래밍", required = false),
            @Parameter(name = "page", description = "페이지", example = "1", required = false),
            @Parameter(name = "size", description = "크기", example = "10", required = false),
    })
    @GetMapping
    public ResponseEntity<Page<ResponseCoursePage>> getCoursePage(
            final CourseSearchCondition courseSearchCondition,
            final Pageable pageable
    ) {
        Page<ResponseCoursePage> coursePage = courseService.getCoursePage(courseSearchCondition, pageable);

        return ResponseEntity.status(OK).body(coursePage);
    }

    @Operation(summary = "강의 조회", description = "course-id에 해당하는 강의 조회")
    @GetMapping("/{course-id}")
    public ResponseEntity<ResponseCourseById> getCourseById(@PathVariable("course-id") final Long courseId) {
        return ResponseEntity.status(OK).body(courseService.getCourseById(courseId));
    }

    @Operation(summary = "강의 생성", description = "강의 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createCourse(
            @Valid @ModelAttribute final CreateCourseForm createCourseForm,
            @AuthPrincipal final Principal principal
    ) {
        return ResponseEntity.status(CREATED).body(courseService.createCourse(createCourseForm, principal));
    }

    @Operation(summary = "강의 수정", description = "course-id에 해당하는 강의 수정")
    @PutMapping(value = "/{course-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateCourseById(
            @Valid @ModelAttribute final UpdateCourseForm updateCourseForm,
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(OK).body(courseService.updateCourseById(updateCourseForm, principal, courseId));
    }

    @Operation(summary = "강의 삭제", description = "course-id에 해당하는 강의 삭제")
    @DeleteMapping("/{course-id}")
    public ResponseEntity<Void> deleteCourseById(
            @AuthPrincipal final Principal principal,
            @PathVariable("course-id") final Long courseId
    ) {
        courseService.deleteCourseById(principal, courseId);
        return ResponseEntity.noContent().build();
    }
}
