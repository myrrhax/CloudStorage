package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public interface UserService {
    UserDto getUser(Long id);
    UserDto getUserByName(String username);
}
