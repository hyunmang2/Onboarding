package com.sparta.onboarding.filter;

import com.sparta.onboarding.jwt.JwtUtil;
import com.sparta.onboarding.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getTokenFromHeader(JwtUtil.ACCESS_HEADER, request);

        if (StringUtils.hasText(tokenValue)) {
            try {
                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token Error");
                }

                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info.getSubject());

            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"리프레시 토큰으로 재발급 받으세요\"}");

                return;
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 다음 filter로 이동
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        // 비어있는 SecurityContex를 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 만든 Authentication 객체에 username을 넣는다.
        Authentication authentication = createAuthentication(username);

        // 비어있는 SecurityContex에 Authentication을 넣고
        context.setAuthentication(authentication);

        // SecurityContextHolder에 Authentication이 들어있는 SecurityContex를 넣는다.
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
