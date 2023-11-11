package com.kangui.talentsharehub.domain.homework.controller;

import com.kangui.talentsharehub.domain.homework.dto.request.CreateHomeworkForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseHomework;
import com.kangui.talentsharehub.domain.homework.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "과제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "과제 조회", description = "course-id에 해당하는 과제 조회")
    @GetMapping
    public ResponseEntity<List<ResponseHomework>> getHomeworksByCourseId(@RequestParam("course-id") Long courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(homeworkService.getHomeworksByCourseId(courseId));
    }

    @Operation(summary = "과제 생성", description = "과제 생성")
    @PostMapping
    public ResponseEntity<Long> createHomework(@Valid @ModelAttribute CreateHomeworkForm requestCreateHomework) {
        return ResponseEntity.status(HttpStatus.CREATED).body(homeworkService.createHomework(requestCreateHomework));
    }

    @Operation(summary = "과제 수정", description = "homework-id에 해당하는 과제 수정")
    @PutMapping("/{homework-id}")
    public ResponseEntity<Long> updateHomework(
            @PathVariable("homework-id") Long homeworkId,
            @Valid @ModelAttribute RequestUpdateHomework requestUpdateHomework) {
        return ResponseEntity.status(HttpStatus.OK).body(homeworkService.updateHomework(homeworkId, requestUpdateHomework));
    }

    @Operation(summary = "과제 삭제", description = "homework-id에 해당하는 과제 삭제")
    @DeleteMapping("/{homework-id}")
    public ResponseEntity<Void> deleteHomework(@PathVariable("homework-id") Long homeworkId) {
        homeworkService.deleteHomework(homeworkId);

        return ResponseEntity.noContent().build();
    }
}
