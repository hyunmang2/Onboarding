package com.sparta.onboarding.service;

import com.sparta.onboarding.entity.User;

public interface RefreshTokenService {

    void updateRefreshToken(User user, String token);
}
