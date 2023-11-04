package com.kangui.talentsharehub.domain.notice.repository;

import com.kangui.talentsharehub.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {
    @Query("SELECT n FROM NOTICE n JOIN FETCH n.user WHERE n.id = :noticeId")
    Optional<Notice> findByIdWithUser(@Param("noticeId") Long noticeId);
}
