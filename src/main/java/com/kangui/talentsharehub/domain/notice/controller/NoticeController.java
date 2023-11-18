package com.kangui.talentsharehub.domain.notice.controller;

import com.kangui.talentsharehub.domain.notice.dto.request.RequestNotice;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticeById;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.service.NoticeService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "공지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{course-id}/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지 조회", description = "공지 조회 (검색 필터링 가능, 페이지네이션)")
    @Parameters({
            @Parameter(name = "search", description = "검색", example = "공지1"),
            @Parameter(name = "course-id", description = "강의 ID", example = "1"),
            @Parameter(name = "page", description = "페이지", example = "1"),
            @Parameter(name = "size", description = "크기", example = "10"),
    })
    @GetMapping
    public ResponseEntity<Page<ResponseNoticePage>> getNoticePage(
            @AuthPrincipal Principal principal,
            @RequestParam("search") final String search,
            @PathVariable("course-id") final Long courseId,
            final Pageable pageable
    ) {
        return ResponseEntity.status(OK).body(noticeService.getNoticePage(principal, search, courseId, pageable));
    }

    @Operation(summary = "공지 조회(notice_id)", description = "notice_id에 해당 하는 공지 조회")
    @GetMapping("/{notice-id}")
    public ResponseEntity<ResponseNoticeById> getNoticeById(
            @AuthPrincipal final Principal principal,
            @PathVariable("notice-id") final Long noticeId,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(OK).body(noticeService.getNoticeById(principal, noticeId, courseId));
    }

    @Operation(summary = "공지 생성", description = "공지 생성")
    @PostMapping
    public ResponseEntity<Long> createNotice(
            @AuthPrincipal Principal principal,
            @Valid @RequestBody final RequestNotice requestNotice,
            @PathVariable("course-id") final Long courseId
    ) {
        return ResponseEntity.status(CREATED).body(noticeService.createNotice(principal, requestNotice, courseId));
    }
}
