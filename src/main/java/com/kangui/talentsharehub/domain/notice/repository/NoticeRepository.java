package com.kangui.talentsharehub.domain.notice.repository;

import com.kangui.talentsharehub.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
