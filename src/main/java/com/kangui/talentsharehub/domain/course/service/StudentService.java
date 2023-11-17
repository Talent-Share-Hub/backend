package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.response.ResponseStudentByCourseId;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Student;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
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
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private CourseRepository courseRepository;

    // 강의 수강생 조회
    @Transactional(readOnly = true)
    public List<ResponseStudentByCourseId> getStudentsByCourseId(final Long courseId) {
        final List<Student> students = studentRepository.findByCourseIdWithUser(courseId);

        return students.stream()
                .map(ResponseStudentByCourseId::of)
                .collect(Collectors.toList());
    }

    // 강의 수강생 등록
    public Long enrollStudent(final Principal principal, final Long courseId) {
        final Users user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 사용자입니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        if(course.isFull()) {
            throw new AppException(ErrorCode.STUDENT_EXCEED, "수강생 인원 초과 입니다.");
        }

        final Student student = new Student(
                user,
                course
        );

        course.incrementEnrollStudents();

        return studentRepository.save(student).getId();
    }

    // 강의 수강생 삭제
    public void removeStudentFromCourse(final Principal principal, final Long courseId) {
        final Student student = studentRepository.findByUserIdAndCourseId(principal.userId(), courseId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND, "존재하지 않는 수강생입니다."));

        studentRepository.delete(student);
    }
}
