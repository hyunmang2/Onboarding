package com.sparta.onboarding.service.impl;

import com.sparta.onboarding.dto.SignupRequestDto;
import com.sparta.onboarding.entity.User;
import com.sparta.onboarding.repository.UserRepository;
import com.sparta.onboarding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();

        // 회원가입 시 비밀번호 양식, 중복된 유저 이름 확인
        validUsername(username);
        validPassword(password);

        // 비밀번호 encode
        String encodePassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .password(encodePassword)
                .nickname(nickname)
                .build();

        userRepository.save(user);
    }

    @Override
    public void validUsername(String username) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("중복된 이름 입니다.");
        }
    }

    @Override
    public void validPassword(String password) {

        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{10,}$")) {
            throw new IllegalArgumentException("비밀번호는 최소 10자 이상이어야하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        }
    }
}
