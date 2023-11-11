package com.kangui.talentsharehub.global.oauth2;

import com.kangui.talentsharehub.domain.user.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

//
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    /*
        OAuth 로그인 시 처음 로그인일 경우, 내 서비스에서 Resource Server가 제공하지 않는
        정보가 필요할 경우에, Resource Server가 아닌 내 서비스에서 해당 정보를 사용자에게 입력 받아야 한다. (생년월일, 소개 등)
     */
    private String loginId;
    /*
        OAuth 로그인 시 위의 추가 정보(생년월일, 소개 등)을 입력했는지(처음 OAuth 로그인인지)를 판단하기 위해 필요
        처음 로그인하는 유저를 Role.GUEST로 설정하고,
        이후에 추가 정보를 입력해서 회원가입을 진행하면, Role.USER로 업데이트 해준다.
        OAuth 로그인 회원 중 Role.GUEST인 회원은 처음 로그인이므로 SuccessHandler에서 추가 정보를 입력하는 URL로 리다이렉트
     */
    private Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String loginId, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.loginId = loginId;
        this.role = role;
    }
}
