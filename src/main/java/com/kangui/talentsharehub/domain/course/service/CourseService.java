package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.request.RequestCreateCourse;
import com.kangui.talentsharehub.domain.course.dto.request.RequestUpdateCourse;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCourseById;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.category.CategoryRepository;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    @Value("${file.course}")
    private String coursePath;

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FileStore fileStore;

    // 강의 조회(페이지네이션)
    public Page<ResponseCoursePage> getCoursePage(CourseSearchCondition courseSearchCondition, Pageable pageable) {
        return courseRepository.getCoursePage(courseSearchCondition, pageable);
    }

    @Transactional
    public Long createCourse(RequestCreateCourse requestCreateCourse) {
        if (!requestCreateCourse.getStartDate().isAfter(requestCreateCourse.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "유효하지 않은 날짜 범위입니다.");
        }

        String courseImageUrl = null;

        try {
            if(requestCreateCourse.getCourseImage() != null && !requestCreateCourse.getCourseImage().isEmpty()) {
                courseImageUrl = fileStore.storeFile(requestCreateCourse.getCourseImage(), coursePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Users teacher = userRepository.findById(requestCreateCourse.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 선생님입니다."));

        Category category = categoryRepository.findById(requestCreateCourse.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 카테고리입니다."));

        return courseRepository.save(requestCreateCourse.toEntity(teacher, category, courseImageUrl)).getId();

    }

    public ResponseCourseById getCourseById(Long courseId) {
        return new ResponseCourseById(courseRepository.findCourseWithUserAndCategoryById(courseId));
    }

    @Transactional
    public Long updateCourseById(Long courseId, RequestUpdateCourse requestUpdateCourse) {
        if (!requestUpdateCourse.getStartDate().isAfter(requestUpdateCourse.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "유효하지 않은 날짜 범위입니다.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        String courseImageUrl = null;

        try {
            if(requestUpdateCourse.getCourseImage() != null && !requestUpdateCourse.getCourseImage().isEmpty()) {
                fileStore.deleteFile(course.getImage_url());
                courseImageUrl = fileStore.storeFile(requestUpdateCourse.getCourseImage(), coursePath);
            }
        } catch (IOException e) {
            log.error("Failed to update course image for course with ID: {}", courseId, e);
        }

        course.updateCourse(requestUpdateCourse, courseImageUrl);

        return courseId;
    }

    @Transactional
    public void deleteCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, ""));

        courseRepository.delete(course);

    }
}
