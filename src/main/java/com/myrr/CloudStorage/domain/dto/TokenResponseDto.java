package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.domain.dto.user.UserDto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        UserDto user
) {
}
