package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.dto.user.UserDto;

public interface UserService {
    UserDto getUser(Long id);
    UserDto getUserByName(String username);
}
