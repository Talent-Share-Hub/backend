package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.request.RequestCreateSyllabus;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseSyllabusByCourseId;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.syllabus.SyllabusRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final CourseRepository courseRepository;

    public List<ResponseSyllabusByCourseId> getSyllabusByCourseId(Long courseId) {
        List<Syllabus> syllabuses = syllabusRepository.findByCourseId(courseId);

        return syllabuses.stream()
                .map(ResponseSyllabusByCourseId::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createSyllabus(RequestCreateSyllabus requestCreateSyllabus) {
        syllabusRepository.findByWeek(requestCreateSyllabus.getWeek())
                .ifPresent(syllabus -> {
                    throw new AppException(ErrorCode.SYLLABUS_DUPLICATED, "이미 존재하는 주차입니다.");
                });
        Course course = courseRepository.findById(requestCreateSyllabus.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        return syllabusRepository.save(requestCreateSyllabus.toEntity(course)).getId();
    }
}
