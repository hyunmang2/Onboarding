package com.sparta.onboarding.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {

    private final String massage;
    private final T data;

    @Builder
    public ResponseDto(String massage, T data) {
        this.massage = massage;
        this.data = data;
    }
}
