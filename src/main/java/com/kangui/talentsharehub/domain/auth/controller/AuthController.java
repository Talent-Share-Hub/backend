package com.kangui.talentsharehub.domain.auth.controller;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestAddInfo;
import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.auth.service.AuthService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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

    @DeleteMapping(value ="/logout")
    public ResponseEntity<Void> logout(@AuthPrincipal Principal principal) {
        authService.logout(principal);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/oauth2/add-info/user/{user-id}")
    public ResponseEntity<Long> addInfo(
            @PathVariable("user-id") final Long userId,
            @RequestBody final RequestAddInfo requestAddInfo
    ) {
        return ResponseEntity.status(CREATED).body(authService.addInfo(userId, requestAddInfo));
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return ResponseEntity.noContent().build();
    }
}
