package com.kangui.talentsharehub.domain.user.controller;

import com.kangui.talentsharehub.domain.user.dto.request.UpdateUserByIdForm;
import com.kangui.talentsharehub.domain.user.dto.response.ResponseUserById;
import com.kangui.talentsharehub.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 조회", description = "해당 user-id의 회원 정보 조회")
    @GetMapping("/{user-id}")
    public ResponseEntity<ResponseUserById> getUserById(@PathVariable("user-id") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    @Operation(summary = "회원 정보 수정", description = "해당 user-id의 회원 정보 수정")
    @PutMapping("/{user-id}")
    public ResponseEntity<Long> updateUser(
            @PathVariable("user-id") Long userId,
            @Valid @ModelAttribute UpdateUserByIdForm updateUserByIdForm) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserById(userId, updateUserByIdForm));
    }

    @Operation(summary = "회원 삭제", description = "해당 user-id의 회원 삭제")
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("user-id") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
