package com.kangui.talentsharehub.domain.course.service;

import com.kangui.talentsharehub.domain.course.dto.request.RequestEnrollStudent;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseStudentByCourseId;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Student;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
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
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private CourseRepository courseRepository;

    // 강의 수강생 조회
    public List<ResponseStudentByCourseId> getStudentsByCourseId(Long courseId) {
        List<Student> students = studentRepository.findByCourseIdWithUser(courseId);

        return students.stream()
                .map(ResponseStudentByCourseId::new)
                .collect(Collectors.toList());
    }

    // 강의 수강생 등록
    @Transactional
    public Long enrollStudent(RequestEnrollStudent requestEnrollStudent) {
        Users user = userRepository.findById(requestEnrollStudent.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 사용자입니다."));

        Course course = courseRepository.findById(requestEnrollStudent.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재하지 않는 강의입니다."));

        if(course.isFull()) {
            throw new AppException(ErrorCode.STUDENT_EXCEED, "수강생 인원 초과 입니다.");
        }

        Student student = Student.builder()
                .user(user)
                .course(course)
                .build();

        course.incrementEnrollStudents();

        return studentRepository.save(student).getId();
    }

    // 강의 수강생 삭제
    @Transactional
    public void removeStudentFromCourse(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND, "존재하지 않는 수강생입니다."));

        studentRepository.delete(student);
    }
}
