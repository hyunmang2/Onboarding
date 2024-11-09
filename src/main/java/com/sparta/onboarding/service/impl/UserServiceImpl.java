package com.sparta.onboarding.service.impl;

import com.sparta.onboarding.dto.LoginRequestDto;
import com.sparta.onboarding.dto.LoginResponseDto;
import com.sparta.onboarding.dto.SignupRequestDto;
import com.sparta.onboarding.entity.RefreshToken;
import com.sparta.onboarding.entity.User;
import com.sparta.onboarding.entity.UserRoleEnum;
import com.sparta.onboarding.jwt.JwtUtil;
import com.sparta.onboarding.repository.RefreshTokenRepository;
import com.sparta.onboarding.repository.UserRepository;
import com.sparta.onboarding.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Value("${ADMIN_KEY}")
    String adminKey;

    @Override
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String nickname = signupRequestDto.getNickname();


        // 회원가입 시 비밀번호 양식, 중복된 유저 이름 확인
        validUsername(username);
        validPassword(password);

        // 비밀번호 encode
        String encodePassword = passwordEncoder.encode(password);

        if (signupRequestDto.getAdminKey() != null) {
            if (signupRequestDto.getAdminKey().equals(adminKey)) {
                User admin = User.builder()
                        .username(username)
                        .password(encodePassword)
                        .nickname(nickname)
                        .role(UserRoleEnum.ADMIN)
                        .build();
                userRepository.save(admin);
            } else {
                throw new IllegalArgumentException("유효하지 않은 ADMIN_KEY 입니다.");
            }
        } else {
            User user = User.builder()
                    .username(username)
                    .password(encodePassword)
                    .nickname(nickname)
                    .role(UserRoleEnum.USER)
                    .build();
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 회원가입 된 유저인지 확인
        User user = getUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 엑세스, 리프레쉬 토큰 생성
        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

        // 헤더에 액세스, 리프레쉬 토큰 저장
        jwtUtil.addJwtToHeader(JwtUtil.ACCESS_HEADER, accessToken, response);
        jwtUtil.addJwtToHeader(JwtUtil.REFRESH_HEADER, refreshToken, response);

        return LoginResponseDto.builder()
                .refreshToken(accessToken)
                .build();
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

    @Override
    public User getUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 회원을 찾을 수 없습니다."));
    }
}
