package com.kangui.talentsharehub.domain.notice.entity;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Users user; // 선생님 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course; // 강의 ID

    private String title; // 제목

    @Lob
    private String contents; // 본문

    private int views; // 조회수

    @Builder
    public Notice(Long id, Users user, Course course, String title, String contents, int views) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.title = title;
        this.contents = contents;
        this.views = views;
    }

    public void changeViews(int newView) {
        this.views = newView;
    }
}
