package com.sparta.onboarding.controller;

import com.sparta.onboarding.dto.ResponseDto;
import com.sparta.onboarding.dto.SignupRequestDto;
import com.sparta.onboarding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<ResponseDto<String>> signup(@RequestBody SignupRequestDto signupRequestDto) {

        userService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.<String>builder()
                        .massage("회원가입에 성공했습니다.")
                        .build());
    }
}
