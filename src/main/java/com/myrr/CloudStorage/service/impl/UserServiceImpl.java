package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.domain.entity.Role;
import com.myrr.CloudStorage.domain.entity.User;
import com.myrr.CloudStorage.domain.enums.RoleType;
import com.myrr.CloudStorage.domain.exceptions.notfound.UserNotFoundException;
import com.myrr.CloudStorage.repository.UserRepository;
import com.myrr.CloudStorage.service.RoleService;
import com.myrr.CloudStorage.service.UserService;
import com.myrr.CloudStorage.utils.mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           RoleService roleService,
                           UserRepository userRepository) {
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(final UserDto dto) {
        User user = this.userMapper.fromDto(dto, false);
        Role role = this.roleService.getRole(RoleType.USER);
        user.addRole(role);
        User savedUser = this.userRepository.save(user);

        return this.userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.map(this.userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
