package com.sparta.onboarding.service;

import com.sparta.onboarding.dto.LoginRequestDto;
import com.sparta.onboarding.dto.LoginResponseDto;
import com.sparta.onboarding.dto.SignupRequestDto;
import com.sparta.onboarding.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    // 회원가입
    void signup(SignupRequestDto signupRequestDto);

    // 로그인
    LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response);

    // 유저이름 확인
    void validUsername(String username);

    // 비밀번호 확인
    void validPassword(String password);

    // 유저이름 찾아오기
    User getUsername(String username);

}
