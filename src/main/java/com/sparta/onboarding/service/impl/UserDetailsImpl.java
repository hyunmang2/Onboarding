package com.sparta.onboarding.service.impl;

import com.sparta.onboarding.entity.User;
import com.sparta.onboarding.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

    @RequiredArgsConstructor
    public class UserDetailsImpl implements UserDetails {

        private final User user;

        public User getUser() {
            return user;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // 사용자의 상태를 가져온다.
            UserRoleEnum role = user.getRole();
            // 가져온 사용자의 상태를 문자열로 바꿔준다.
            String authority = role.getAuthority();

            // simpleGrantedAuthority 객체를 만들어준다.
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // authorities 컬렉션에 권한이 들어있는 객체를 넣어준다.
            authorities.add(simpleGrantedAuthority);

            return authorities;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }
