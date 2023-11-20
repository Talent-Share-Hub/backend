package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.request.RequestCreateSyllabus;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseSyllabusByCourseId;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.syllabus.SyllabusRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<ResponseSyllabusByCourseId> getSyllabusByCourseId(final Long courseId) {
        List<Syllabus> syllabuses = syllabusRepository.findByCourseId(courseId);

        return syllabuses.stream()
                .map(ResponseSyllabusByCourseId::of)
                .collect(Collectors.toList());
    }

    public Long createSyllabus(
            final RequestCreateSyllabus requestCreateSyllabus, final Principal principal, final Long courseId
    ) {
        if (syllabusRepository.existsByWeek(requestCreateSyllabus.getWeek())) {
            throw new AppException(ErrorCode.SYLLABUS_DUPLICATED, "이미 존재하는 주차입니다.");
        }

        final Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        if (!course.getUser().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "강의를 수정할 권한이 없습니다.");
        }

        Syllabus syllabus = new Syllabus(
                course,
                requestCreateSyllabus.getWeek(),
                requestCreateSyllabus.getCourseContent()
        );

        return syllabusRepository.save(syllabus).getId();
    }

}
