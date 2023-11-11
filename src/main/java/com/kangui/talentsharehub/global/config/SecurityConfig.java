package com.kangui.talentsharehub.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.kangui.talentsharehub.global.login.handler.LoginFailureHandler;
import com.kangui.talentsharehub.global.login.handler.LoginSuccessHandler;
import com.kangui.talentsharehub.global.login.service.LoginService;
import com.kangui.talentsharehub.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.kangui.talentsharehub.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.kangui.talentsharehub.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
// @Import로 Spring Security와 관련된 클래스들을 import해준다. (Spring Security를 활성화시키는 어노테이션이다.)
@EnableWebSecurity
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final PasswordEncoder passwordEncoder;

    // Spring Security 5.7버전 이상부터는 Spring Security의 설정 시에 설정 메서드를 @Bean을 통해 빈으로 등록해서 컨테이너가 관리하도록 한다.
    // 이전에는 빈 등록 대신 WebSecurityConfigurerAdapter를 상속받아 메서드를 Override했다.
    /*
        세부적인 보안 기능 설정 API를 제공하는 HttpSecurity를 파라미터로 받아서
        HttpSecurity 보안 기능 설정 API를 사용하여 최종적으로 여러 설정을 마친 SecurityFilterChain 객체를 반환한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                /*
                 Spring Security에서는 아무 설정을 하지 않으면,
                 기본으로 FormLogin 형식의 로그인을 제공한다. (자체 Login으로 로그인을 진행하기 때문에 disable)
                 */
                .formLogin(AbstractHttpConfigurer::disable) // FormLogin 사용  X
                // JWT 토큰을 사용한 로그인(Bearer 방식)이기 때문에 기본 설정인 httpBasic disable
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 사용 X
                /*
                REST API를 사용하여 서버에 인증 정보를 저장하지 않고, 요청시 인증 정보(JWT 토큰, OAuth2)를 담아서 요청하므로
                보안 기능인 csrf가 필요가 없으므로 disable
                 */
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X
                /*
                Spring Security는 기본적으로 X-Frame-Options Click jacking 공격 막기 설정이 되어있다.
                h2 콘솔(iframe) 사용을 위해 disable
                 */
                .headers(httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                // 세션을 사용하지 않으므로 STATELESS로 설정
                .sessionManagement(httpSecuritySessionManagementConfigurer ->  {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // URL 별 권한 관리
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            // 인증 절차 없이 접근할 URL 지정
                            .requestMatchers("/", "/favicon.ico").permitAll()
                            .requestMatchers("/login", "/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                            // 나머지 리소스는 인증 절차를 거친 사용자만 접근이 가능하다는 의미
                            .anyRequest().authenticated();
                })
                // 소셜 로그인 설정
                // Http Secyrity의 OAuth2 로그인 관련 OAuth2LoginConfigurer를 통해 OAuth2 로그인에 관련된 다양한 기능을 사용할 수 있도록 한다.
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        // OAuth2 로그인이 성공했을 때 처리할 핸들러 설정
                        .successHandler(oAuth2LoginSuccessHandler)
//                        // 실패했을 때
//                        .failureHandler(oAuth2LoginFailureHandler)
//                        // OAuth2 로그인의 로직을 담당하는 Service 설정
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                )
                /*
                    원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
                    따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
                    순서: LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter()
                 */
                .addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                // 커스텀 JSON 로그인 필터 동작 전에 JWT 인증 필터 동작 설정
                .addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class)
                .build();
    }



    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, userRepository);
    }
}