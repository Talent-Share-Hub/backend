package com.kangui.talentsharehub.domain.auth.controller;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestAddInfo;
import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "사용자 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> signUp(@Valid @ModelAttribute final SignUpForm signUpForm) {
        return ResponseEntity.status(CREATED).body(authService.signUp(signUpForm));
    }

    @PostMapping("/oauth2/add-info")
    public ResponseEntity<Long> addInfo(
            @RequestParam("user-id")final Long userId,
            @RequestBody final RequestAddInfo requestAddInfo
    ) {
        return ResponseEntity.status(CREATED).body(authService.addInfo(userId, requestAddInfo));
    }
}
