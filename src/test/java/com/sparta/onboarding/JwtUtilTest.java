package com.sparta.onboarding;

import com.sparta.onboarding.entity.UserRoleEnum;
import com.sparta.onboarding.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtilTest {

    private static JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @BeforeEach
    public void setUp() {

        jwtUtil = new JwtUtil();
        secretKey = "7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg==";
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);

    }

    @Test
    @DisplayName("AccessToken 생성 테스트")
    public void testCreateAccessToken() {

        String username = "testUser";
        UserRoleEnum role = UserRoleEnum.USER;

        Date date = new Date();

                String jwts = Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();

        Assert.notNull(jwts, "AccessToken이  없습니다.");
    }
}
