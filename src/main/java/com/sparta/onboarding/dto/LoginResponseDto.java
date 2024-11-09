package com.sparta.onboarding.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String refreshToken;

    @Builder
    public LoginResponseDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
