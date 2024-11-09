package com.sparta.onboarding.jwt;

import com.sparta.onboarding.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Access Token Hearder Key 값
    public static final String ACCESS_HEADER = "Access";
    // Refresh Token Header KEY 값
    public static final String REFRESH_HEADER = "refresh";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // AccessToken 만료시간 60분
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;
    // RefreshToken 만료시간 14일
    private final long REFRESHTOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;

    // Base64 Encode 한 SecretKey
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 딱 한번만 받아오면 되는 값을 사용 할 때마다 새로 요청하는 실수를 방지하기 위한 메서드
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 토큰 생성
    public String createAccessToken(String userName, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userName) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + REFRESHTOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();

    }

    public void addJwtToHeader(String tokenHeader, String token, HttpServletResponse res) {
        res.addHeader(tokenHeader, token);
    }

    // token을 header에서 가져와서 반환하는 메서드
    public String getTokenFromHeader(String tokenHeader, HttpServletRequest request) {
        // header에서 token을 가져온다.
        String token = request.getHeader(tokenHeader);
        // 공백(null)인지 && BEARER로 시작을 하는지 확인
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            // 둘 다 만족할 경우 BEARER 뒤에 공백 길이만큼 잘라내고 순수한 토큰 값만을 리턴
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

//    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
