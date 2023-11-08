package com.kangui.talentsharehub.domain.course.repository.course;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.response.QResponseCoursePage;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.QCourseImageFile;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.kangui.talentsharehub.domain.course.entity.QCategory.*;
import static com.kangui.talentsharehub.domain.course.entity.QCourse.*;
import static com.kangui.talentsharehub.domain.course.entity.QCourseImageFile.*;
import static com.kangui.talentsharehub.domain.rating.entity.QTotalRating.*;
import static com.kangui.talentsharehub.domain.user.entity.QUsers.*;

public class CourseRepositoryImpl implements CourseCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CourseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseCoursePage> getCoursePage(CourseSearchCondition condition, Pageable pageable) {
        List<ResponseCoursePage> content = queryFactory
                .select(
                        new QResponseCoursePage(
                                course.id,
                                course.user.nickname.as("teacherName"),
                                course.courseImageFile.fileUrl,
                                course.title,
                                course.capacity,
                                course.enrolledStudents,
                                course.courseStatus,
                                course.dateRange.startDate,
                                course.dateRange.endDate
                        )
                )
                .from(course)
                .leftJoin(course.user, users)
                .leftJoin(course.category, category)
                .leftJoin(course.courseImageFile, courseImageFile)
                .where(
                        search(condition.getSearch()),
                        categoryEq(condition.getCategory())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Course> countQuery = queryFactory
                .selectFrom(course)
                .leftJoin(course.user, users)
                .leftJoin(course.category, category)
                .leftJoin(course.courseImageFile, courseImageFile)
                .where(
                        search(condition.getSearch()),
                        categoryEq(condition.getCategory())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression search(String search) {
        return StringUtils.hasText(search) ? course.title.likeIgnoreCase("%" + search + "%") : null;
    }

    private BooleanExpression categoryEq(String category) {
        return StringUtils.hasText(category) ? course.category.name.eq(category)  : null;
    }
}
