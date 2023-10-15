package com.kangui.talentsharehub.global.oauth2.service;

import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.enums.SocialType;
import com.kangui.talentsharehub.global.oauth2.OAuth2CustomUser;
import com.kangui.talentsharehub.global.oauth2.OAuthAttributes;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

// loadUser 메서드가 실행될 시점엔 이미 Access Token이 정상적으로 발급된 상태이며
// super.loadUser 메서드를 통해 Access Token으로 User 정보를 조회해 온다.
// 해당 정보를 통해 회원가입 또는 회원 정보 갱신 로직 진행
// 최종적으로 security가 인증 여부를 확인할 수 있도록 OAuth2User 객체를 반환
// 마지막 과정으로 oAuth2AuthenticationSuccessHandler가 호출되는데, 해당 핸들러의 동작 과정에서 사용자 정보를 가지고
// JwtService 통해 실제 사용될 access token을 발급하게 된다. (oauth 동작 과정에서의 access token과는 다르다)
@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        // OAuth 서비스에서 가져온 유저 정보를 담고 있는 객체
        OAuth2User oAuth2User = service.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스(구글/카카오 등)를 구분하는 코드
        // http://host//oauth2/authorization/kakao에서 kakao가 registrationId
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        // OAuth2 로그인 진행 시 키가 되는 필드 값 (Primary Key와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // 소셜 로그인에서 API가 제공하는 userInfo의 JSON 값 (유저 정보들)
        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 서비스 유형에 맞게 담을 클래스
        // oAuth2User.getAttributes() : OAuth2User의 attribute
        OAuthAttributes attributes = OAuthAttributes
                .of(socialType, userNameAttributeName, originAttributes);

        // User 객체 생성
        Users user = saveUser(attributes, socialType);

        log.info("user: {}", user);

        return new OAuth2CustomUser(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                originAttributes,
                attributes.getNameAttributeKey(),
                user.getLoginId(),
                user.getRole()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if("naver".equals(registrationId)) {
            return SocialType.NAVER;
        }
        if("kakao".equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    /*
        SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메서드
        만약 찾은 회원이 있드면, 그대로 반환하고 없다면 save()를 호출하여 회원 저장
     */
    private Users saveUser(OAuthAttributes attributes, SocialType socialType) {
        Users user = userRepository
                .findBySocialTypeAndSocialId(socialType, attributes.getOAuth2UserInfo().getId())
                .orElse(attributes.toEntity(socialType, attributes.getOAuth2UserInfo()));

        return userRepository.save(user);
    }


}
