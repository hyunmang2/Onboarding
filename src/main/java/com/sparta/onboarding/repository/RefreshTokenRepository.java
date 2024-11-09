package com.sparta.onboarding.repository;

import com.sparta.onboarding.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    @Transactional
    @Query("update RefreshToken rt set rt.refreshToken = :newToken where rt.user.id = :userId")
    void updateByUserId(Long userId, String newToken);
}
