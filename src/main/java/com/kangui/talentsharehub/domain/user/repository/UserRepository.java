package com.kangui.talentsharehub.domain.user.repository;

import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u JOIN FETCH u.userImageFile where u.id = :userId")
    Optional<Users> findByIdWithUserImageFile(@Param("userId") Long userId);

    Optional<Users> findByLoginId(String loginId);

    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByRefreshToken(String refreshToken);

    /*
    소셜 타입과 소셜의 식별값으로 회원을 찾는 메서드
    정보 제공을 동의한 순간 DB에 저장해야하지만, 아직 추가 정보(나이, 소개 등)를 입력받지 않았으므로
    유저 객체는 DB에 있지만, 추가정보가 빠진 상태이다.
    따라서 추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메서드
     */
    Optional<Users> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);
}
