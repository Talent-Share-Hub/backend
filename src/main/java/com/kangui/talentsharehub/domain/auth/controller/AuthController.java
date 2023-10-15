package com.kangui.talentsharehub.domain.auth.controller;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestSignUp;
import com.kangui.talentsharehub.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService userService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody RequestSignUp requestSignUp) {
        userService.signUp(requestSignUp);
        return "회원가입 성공";
    }
}
