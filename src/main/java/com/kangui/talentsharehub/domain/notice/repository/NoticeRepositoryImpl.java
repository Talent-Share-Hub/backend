package com.kangui.talentsharehub.domain.notice.repository;

import com.kangui.talentsharehub.domain.course.entity.QCourse;
import com.kangui.talentsharehub.domain.notice.dto.response.QResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.kangui.talentsharehub.domain.course.entity.QCourse.*;
import static com.kangui.talentsharehub.domain.notice.entity.QNotice.*;
import static com.kangui.talentsharehub.domain.user.entity.QUsers.*;

public class NoticeRepositoryImpl implements NoticeCustomRepository{

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseNoticePage> getNoticePage(String search, Long courseId, Pageable pageable) {
        List<ResponseNoticePage> content = queryFactory
                .select(
                        new QResponseNoticePage(
                                notice.id,
                                users.nickname.as("teacherName"),
                                notice.title,
                                notice.views
                        )
                )
                .from(notice)
                .leftJoin(notice.user, users)
                .where(
                        search(search),
                        notice.course.id.eq(courseId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Notice> countQuery = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.user, users)
                .where(
                        search(search),
                        notice.course.id.eq(courseId)
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression search(String search) {
        return StringUtils.hasText(search) ?
                notice.title.likeIgnoreCase("%" + search + "%")
                        .or(notice.contents.likeIgnoreCase("%" + search + "%")) : null;
    }
}
