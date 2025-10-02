package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.domain.entity.RefreshToken;
import com.myrr.CloudStorage.domain.entity.Role;
import com.myrr.CloudStorage.domain.entity.User;
import com.myrr.CloudStorage.domain.enums.RoleType;
import com.myrr.CloudStorage.domain.exceptions.conflict.UsernameOrEmailAlreadyExistsException;
import com.myrr.CloudStorage.domain.exceptions.notfound.UserNotFoundException;
import com.myrr.CloudStorage.domain.exceptions.unathorized.InvalidRefreshTokenException;
import com.myrr.CloudStorage.repository.UserRepository;
import com.myrr.CloudStorage.service.AuthService;
import com.myrr.CloudStorage.service.RefreshTokenService;
import com.myrr.CloudStorage.utils.jwt.JwtUtils;
import com.myrr.CloudStorage.utils.mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserMapper userMapper, RefreshTokenService refreshTokenService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleServiceImpl roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public TokenResponseDto register(UserDto dto) {
        User createdUser = saveUser(dto);
        RefreshToken preparedToken = this.jwtUtils.generateRefreshToken(createdUser);
        RefreshToken savedToken = this.refreshTokenService.save(preparedToken);
        String accessToken = this.jwtUtils.generateToken(createdUser);

        return new TokenResponseDto(accessToken, savedToken.getToken(), this.userMapper.toDto(createdUser));
    }

    @Override
    public TokenResponseDto login(UserDto dto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                dto.name(),
                dto.password()
        );
        this.authenticationManager.authenticate(authentication);
        User user = this.userRepository.findByName(dto.name())
                .get();
        RefreshToken refreshToken = this.jwtUtils.generateRefreshToken(user);
        this.refreshTokenService.save(refreshToken);
        String accessToken = this.jwtUtils.generateToken(user);

        return new TokenResponseDto(accessToken, refreshToken.getToken(), this.userMapper.toDto(user));
    }

    @Override
    public void logout(String refresh) {
        RefreshToken token = this.refreshTokenService.getToken(refresh)
                .orElseThrow(InvalidRefreshTokenException::new);
        this.refreshTokenService.delete(token);
    }

    @Override
    public TokenResponseDto refresh(String refreshToken) {
        RefreshToken token = this.refreshTokenService.getToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);
        User user = token.getUser();
        RefreshToken newToken = this.jwtUtils.generateRefreshToken(user);
        this.refreshTokenService.delete(token);
        this.refreshTokenService.save(newToken);
        String newAccessToken = this.jwtUtils.generateToken(user);
        return new TokenResponseDto(newAccessToken, newToken.getToken(), this.userMapper.toDto(user));
    }

    @Override
    public UserDto getMe(Principal principal) {
        var user = this.userRepository.findByName(principal.getName())
                .orElseThrow(() -> new UserNotFoundException(principal.getName()));

        return this.userMapper.toDto(user);
    }

    private User saveUser(final UserDto dto) {
        User user = this.userMapper.fromDto(dto, false);
        Role role = this.roleService.getRole(RoleType.USER);
        user.addRole(role);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        try {
            return this.userRepository.save(user);
        } catch (Exception exception) {
            throw new UsernameOrEmailAlreadyExistsException(user.getName(), user.getEmail());
        }
    }
}
