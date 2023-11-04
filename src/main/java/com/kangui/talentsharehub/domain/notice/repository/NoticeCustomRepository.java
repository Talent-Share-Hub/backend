package com.kangui.talentsharehub.domain.notice.repository;

import com.kangui.talentsharehub.domain.notice.dto.response.ResponseNoticePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeCustomRepository {

    Page<ResponseNoticePage> getNoticePage(String search, Long courseId, Pageable pageable);
}
