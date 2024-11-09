package com.sparta.onboarding.service;

import com.sparta.onboarding.dto.SignupRequestDto;

public interface UserService {

    // 회원가입
    void signup(SignupRequestDto signupRequestDto);

    // 유저이름 확인
    void validUsername(String username);

    // 비밀번호 확인
    void validPassword(String password);
}
