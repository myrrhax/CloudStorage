package com.myrr.CloudStorage.utils.mapping;

import com.myrr.CloudStorage.domain.dto.user.RegisterUserDto;
import com.myrr.CloudStorage.domain.dto.user.UserDto;
import com.myrr.CloudStorage.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Value("${file.server.path}")
    private String fileServerPath;

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toSet()),
                user.getConfirmed(),
                user.getAvatar() != null
                        ? fileServerPath + "/files/" + user.getAvatar().getId()
                        : null
        );
    }

    public User fromDto(RegisterUserDto dto) {
        return new User(
                dto.name(),
                dto.email(),
                dto.password()
        );
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }
}
