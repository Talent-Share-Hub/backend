package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.request.CreateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.request.UpdateCourseForm;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCourseById;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.course.repository.category.CategoryRepository;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.courseimagefile.CourseImageFileRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;

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
    private final CourseImageFileRepository courseImageFileRepository;
    private final FileStore fileStore;

    // 강의 조회(페이지네이션)
    public Page<ResponseCoursePage> getCoursePage(CourseSearchCondition courseSearchCondition, Pageable pageable) {
        return courseRepository.getCoursePage(courseSearchCondition, pageable);
    }

    @Transactional
    public Long createCourse(CreateCourseForm createCourseForm) {
        if (!createCourseForm.getStartDate().isAfter(createCourseForm.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }

        UploadFile uploadFile = null;

        try {
            uploadFile = fileStore.storeFile(createCourseForm.getCourseImage(), coursePath);
        } catch (IOException e) {
            log.error("Error storing course image", e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "강의 이미지 생성에 실패 했습니다.");
        }

        Users teacher = userRepository.findById(createCourseForm.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 선생님입니다."));

        Category category = categoryRepository.findById(createCourseForm.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 카테고리입니다."));

        CourseImageFile courseImageFile = CourseImageFile.builder()
                .storeFileName(uploadFile.getStoreFileName())
                .uploadFileName(uploadFile.getUploadFileName())
                .fileUrl(uploadFile.getFileUrl())
                .build();

        return courseRepository.save(createCourseForm.toEntity(teacher, category, courseImageFile)).getId();

    }

    public ResponseCourseById getCourseById(Long courseId) {
        return new ResponseCourseById(courseRepository.findCourseWithUserAndCategoryAndCourseImageFileById(courseId));
    }

    @Transactional
    public Long updateCourseById(Long courseId, UpdateCourseForm updateCourseForm) {
        if (!updateCourseForm.getStartDate().isAfter(updateCourseForm.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }

        Course course = courseRepository.findByIdWithCourseImageFile(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        UploadFile uploadFile = null;

        try {
            fileStore.deleteFile(course.getCourseImageFile().getStoreFileName(), coursePath);
            uploadFile = fileStore.storeFile(updateCourseForm.getCourseImage(), coursePath);
        } catch (IOException e) {
            log.error("Failed to update course image for course with ID: {}", courseId, e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "강의 이미지 업로드에 실패 했습니다.");
        }

        course.updateCourse(updateCourseForm);
        course.getCourseImageFile().updateImageFile(uploadFile.getUploadFileName(), uploadFile.getStoreFileName(), uploadFile.getFileUrl());

        return courseId;
    }

    @Transactional
    public void deleteCourseById(Long courseId) {
        Course course = courseRepository.findByIdWithCourseImageFile(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, ""));

        try {
            fileStore.deleteFile(course.getCourseImageFile().getStoreFileName(), coursePath);
        } catch (MalformedURLException e) {
            log.error("강의 이미지 삭제에 실패 했습니다. couserId: {}", courseId, e);
            throw new AppException(ErrorCode.FILE_DELETE_FAILED, "강의 이미지 삭제에 실패 했습니다");
        }

        courseRepository.delete(course);
    }
}
