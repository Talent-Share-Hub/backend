package com.kangui.talentsharehub.domain.user.repository;

import com.kangui.talentsharehub.domain.user.entity.UserImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImageFileRepository extends JpaRepository<UserImageFile, Long> {

    @Query("SELECT uif FROM UserImageFile uif WHERE uif.storeFileName = :storeFileName")
    Optional<UserImageFile> findByStoreFileName(@Param("storeFileName") String storeFileName);
}
