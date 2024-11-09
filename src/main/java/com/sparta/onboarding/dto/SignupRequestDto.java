package com.sparta.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값 입니다.")
    private String nickname;

    private String adminKey;
}
