package com.kangui.talentsharehub.domain.notice.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.notice.dto.request.RequestNotice;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticeById;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import com.kangui.talentsharehub.domain.notice.repository.NoticeRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Page<ResponseNoticePage> getNoticePage(String search, Long courseId, Pageable pageable) {
        return noticeRepository.getNoticePage(search, courseId, pageable);
    }

    @Transactional
    public Long createNotice(RequestNotice requestNotice) {
        Course course = courseRepository.findById(requestNotice.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));
        Users users = userRepository.findById(requestNotice.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재 하지 않는 사용자 입니다."));

        return noticeRepository.save(requestNotice.toEntity(course, users)).getId();
    }

    @Transactional
    public ResponseNoticeById getNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findByIdWithUser(noticeId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND, "존재 하지 않는 공지 입니다."));

        notice.changeViews(notice.getViews() + 1);

        return ResponseNoticeById.builder().notice(notice).build();
    }
}
