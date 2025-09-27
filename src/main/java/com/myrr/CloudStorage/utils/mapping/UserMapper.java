package com.myrr.CloudStorage.utils.mapping;

import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null,
                user.getRoles().stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toSet()),
                user.getConfirmed(),
                user.getAvatarUrl()
        );
    }

    public User fromDto(UserDto dto, boolean isConfirmed) {
        return new User(
            dto.name(),
            dto.email(),
            dto.password(),
            isConfirmed,
            dto.avatarUrl()
        );
    }
}
