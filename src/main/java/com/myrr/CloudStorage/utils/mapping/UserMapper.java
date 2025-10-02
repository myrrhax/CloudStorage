package com.myrr.CloudStorage.utils.mapping;

import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Value("file.server.path")
    private String fileServerPath;
    private final UriComponentsBuilder componentsBuilder;

    @Autowired
    public UserMapper(UriComponentsBuilder componentsBuilder) {
        this.componentsBuilder = componentsBuilder;
    }

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
                user.getAvatar() != null
                        ? this.componentsBuilder
                            .path(fileServerPath)
                            .buildAndExpand(user.getAvatar().getId())
                            .toUriString()
                        : null
        );
    }

    public User fromDto(UserDto dto, boolean isConfirmed) {
        return new User(
            dto.name(),
            dto.email(),
            dto.password()
        );
    }
}
