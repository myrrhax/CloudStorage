package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.UserDto;

public interface AuthService {
    TokenResponseDto register(UserDto dto);
    TokenResponseDto login(UserDto dto);
    void logout(String refresh);
    TokenResponseDto refresh(String refreshToken);
}
