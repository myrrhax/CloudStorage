package com.myrr.CloudStorage.domain.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        UserDto user
) {
}
