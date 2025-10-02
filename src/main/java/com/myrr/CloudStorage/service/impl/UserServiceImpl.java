package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.domain.entity.Role;
import com.myrr.CloudStorage.domain.entity.User;
import com.myrr.CloudStorage.domain.enums.RoleType;
import com.myrr.CloudStorage.domain.exceptions.conflict.UsernameOrEmailAlreadyExistsException;
import com.myrr.CloudStorage.domain.exceptions.notfound.UserNotFoundException;
import com.myrr.CloudStorage.repository.UserRepository;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.RoleService;
import com.myrr.CloudStorage.service.UserService;
import com.myrr.CloudStorage.utils.jwt.JwtUtils;
import com.myrr.CloudStorage.utils.mapping.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.map(this.userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDto getUserByName(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return this.userMapper.toDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new JwtEntity(
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getRoles()
                    .stream()
                    .map(role -> role.getRole().name())
                    .map(SimpleGrantedAuthority::new)
                    .toList()
        );
    }
}
