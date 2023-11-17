package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.request.CreateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.request.UpdateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCourseById;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.repository.category.CategoryRepository;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    @Value("${file.course}")
    private String coursePath;

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FileStore fileStore;


    @Transactional(readOnly = true)
    public Page<ResponseCoursePage> getCoursePage(
            final CourseSearchCondition courseSearchCondition,
            final Pageable pageable
    ) {
        return courseRepository.getCoursePage(courseSearchCondition, pageable);
    }

    @Transactional(readOnly = true)
    public ResponseCourseById getCourseById(final Long courseId) {
        final Course course = courseRepository.findCourseWithUserAndCategoryAndCourseImageFileById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        return ResponseCourseById.of(course);
    }

    public Long createCourse(final CreateCourseForm createCourseForm, final Principal principal, final Long courseId) {
        final Users teacher = userRepository.findById(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다."));

        final Category category = categoryRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 카테고리입니다."));

        final UploadFile uploadFile = fileStore.storeFile(createCourseForm.getCourseImage(), coursePath);

        final Course course = new Course(
                teacher,
                category,
                null,
                createCourseForm.getTitle(),
                createCourseForm.getDescription(),
                createCourseForm.getReference(),
                createCourseForm.getLink(),
                createCourseForm.getContact(),
                createCourseForm.getCapacity(),
                new DateRange(
                        createCourseForm.getStartDate(),
                        createCourseForm.getEndDate()
                )
        );
        CourseImageFile courseImageFile = uploadFile.toCourseImageFile();
        course.changeCourseImageFile(courseImageFile);

        return courseRepository.save(course).getId();
    }

    public Long updateCourseById(final UpdateCourseForm updateCourseForm, final Principal principal, final Long courseId) {
        Course course = courseRepository.findByIdWithCourseImageFile(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        permissionCheck(principal, course);

        final UploadFile uploadFile =
                fileStore.updateFile(
                        updateCourseForm.getCourseImage(),
                        course.getCourseImageFile().getStoreFileName(),
                        coursePath);

        course.updateCourse(updateCourseForm);
        course.getCourseImageFile().updateImageFile(uploadFile.getUploadFileName(),
                                                    uploadFile.getStoreFileName(),
                                                    uploadFile.getFileUrl());

        return courseId;
    }

    public void deleteCourseById(final Principal principal, final Long courseId) {
        final Course course = courseRepository.findByIdWithCourseImageFile(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, ""));

        permissionCheck(principal, course);

        fileStore.deleteFile(course.getCourseImageFile().getStoreFileName(), coursePath);

        courseRepository.delete(course);
    }

    private void permissionCheck(final Principal principal, final Course course) {
        if (!course.getUser().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "강의를 수정할 권한이 없습니다.");
        }
    }
}
