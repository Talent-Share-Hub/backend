package com.kangui.talentsharehub.domain.notice.controller;

import com.kangui.talentsharehub.domain.notice.dto.request.RequestNotice;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticeById;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
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
    public ResponseEntity<Page<ResponseNoticePage>> getNoticePage(@RequestParam("search") String search, @RequestParam("course-id") Long courseId, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticePage(search, courseId, pageable));
    }

    @Operation(summary = "공지 조회(notice_id)", description = "notice_id에 해당 하는 공지 조회")
    @GetMapping("/{notice-id}")
    public ResponseEntity<ResponseNoticeById> getNoticeById(@PathVariable("notice-id") Long noticeId) {
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.getNoticeById(noticeId));
    }

    @Operation(summary = "공지 생성", description = "공지 생성")
    @PostMapping
    public ResponseEntity<Long> createNotice(@RequestBody RequestNotice requestNotice) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(requestNotice));
    }
}
