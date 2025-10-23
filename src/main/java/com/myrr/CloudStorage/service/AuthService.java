package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.user.LoginUserDto;
import com.myrr.CloudStorage.domain.dto.user.RegisterUserDto;
import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.user.UserDto;

public interface AuthService {
    TokenResponseDto register(RegisterUserDto dto);
    TokenResponseDto login(LoginUserDto dto);
    void logout(String refresh);
    TokenResponseDto refresh(String refreshToken);
}
