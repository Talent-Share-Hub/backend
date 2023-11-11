package com.kangui.talentsharehub.domain.auth.controller;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestAddInfo;
import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> signUp(@Valid @ModelAttribute SignUpForm signUpForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpForm));
    }

    @PostMapping("/oauth2/add-info")
    public ResponseEntity<Long> addInfo(@RequestParam("login-id") String loginId, @RequestBody RequestAddInfo requestAddInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.addInfo(loginId, requestAddInfo));
    }
}
