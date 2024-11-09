package com.sparta.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
}
