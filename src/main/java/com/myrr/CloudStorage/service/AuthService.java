package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.UserDto;

import java.security.Principal;

public interface AuthService {
    TokenResponseDto register(UserDto dto);
    TokenResponseDto login(UserDto dto);
    void logout();
    TokenResponseDto refresh(String refreshToken);
    UserDto getMe(Principal principal);
}
