package com.kangui.talentsharehub.domain.notice.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.notice.dto.request.RequestNotice;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticeById;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import com.kangui.talentsharehub.domain.notice.repository.NoticeRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ResponseNoticePage> getNoticePage(
            final Principal principal, final String search, final Long courseId, final Pageable pageable) {
        if(studentRepository.existsByCourseIdAndUserId(courseId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "공지를 조회할 권한이 없습니다.");
        }

        return noticeRepository.getNoticePage(search, courseId, pageable);
    }

    public ResponseNoticeById getNoticeById(final Principal principal, final Long noticeId, final Long courseId) {
        Notice notice = noticeRepository.findByIdWIthCourse(noticeId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTICE_NOT_FOUND, "존재 하지 않는 공지 입니다."));

        if(!(notice.getCourse().getId().equals(courseId) ||
                courseRepository.existsByCourseIdAndTeacherId(courseId, principal.userId()) ||
                studentRepository.existsByCourseIdAndUserId(courseId, principal.userId()))) {
            throw new AppException(ErrorCode.FORBIDDEN, "공지를 조회할 권한이 없습니다.");
        }

        notice.increaseView();

        return ResponseNoticeById.of(notice);
    }

    public Long createNotice(final Principal principal, final RequestNotice requestNotice, final Long courseId) {
        final Course course = courseRepository.findByIdWithUser(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        if(!course.getUser().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "공지사항은 선생님만 작성할 수 있습니다.");
        }

        final Users user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재 하지 않는 사용자 입니다."));

        final Notice notice = new Notice(
                user,
                course,
                requestNotice.getTitle(),
                requestNotice.getContents()
        );

        return noticeRepository.save(notice).getId();
    }

}
