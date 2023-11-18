package com.kangui.talentsharehub.domain.user.controller;

import com.kangui.talentsharehub.domain.user.dto.request.UpdateUserByIdForm;
import com.kangui.talentsharehub.domain.user.dto.response.ResponseUserById;
import com.kangui.talentsharehub.domain.user.service.UserService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 조회", description = "해당 user-id의 회원 정보 조회")
    @GetMapping("/{user-id}")
    public ResponseEntity<ResponseUserById> getUserById(@PathVariable("user-id") final Long userId) {
        return ResponseEntity.status(OK).body(userService.getUserById(userId));
    }

    @Operation(summary = "회원 정보 수정", description = "해당 user-id의 회원 정보 수정")
    @PutMapping
    public ResponseEntity<Long> updateUser(
            @AuthPrincipal final Principal principal,
            @Valid@ModelAttribute final UpdateUserByIdForm updateUserByIdForm) {
        return ResponseEntity.status(OK).body(userService.updateUserById(principal, updateUserByIdForm));
    }

    @Operation(summary = "회원 삭제", description = "해당 user-id의 회원 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteUserById(@AuthPrincipal final Principal principal) {
        userService.deleteUserById(principal);
        return ResponseEntity.noContent().build();
    }

}
