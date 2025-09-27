package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto dto);
    UserDto getUser(Long id);
}
