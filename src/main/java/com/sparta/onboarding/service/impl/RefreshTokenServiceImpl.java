package com.sparta.onboarding.service.impl;

import com.sparta.onboarding.entity.RefreshToken;
import com.sparta.onboarding.entity.User;
import com.sparta.onboarding.repository.RefreshTokenRepository;
import com.sparta.onboarding.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void updateRefreshToken(User user, String token) {

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(user.getId());

        if (optionalRefreshToken.isPresent()) {
            // 기존 토큰이 존재하는 경우, 해당 토큰을 업데이트합니다.
            refreshTokenRepository.updateByUserId(user.getId(), token);
        } else {
            // 기존 토큰이 존재하지 않는 경우, 새로운 토큰을 생성하여 저장합니다.
            RefreshToken newToken = RefreshToken.builder()
                    .user(user)
                    .refreshToken(token)
                    .build();
            refreshTokenRepository.save(newToken);
        }
    }
}
