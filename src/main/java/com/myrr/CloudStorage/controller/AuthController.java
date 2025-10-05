package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.service.AuthService;
import com.myrr.CloudStorage.utils.validation.markers.Login;
import com.myrr.CloudStorage.utils.validation.markers.OnCreate;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody @Validated({Default.class, OnCreate.class}) UserDto dto,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        TokenResponseDto resultDto = this.authService.register(dto);
        UriComponents location = uriComponentsBuilder
                .path("/api/auth") // ToDo change
                .buildAndExpand(resultDto.user().id());

        return ResponseEntity.created(location.toUri())
                .body(resultDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Validated({Login.class}) UserDto dto) {
        TokenResponseDto resultDto = this.authService.login(dto);

        return ResponseEntity.ok()
                .body(resultDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody final String refresh) {
        TokenResponseDto resultDto = this.authService.refresh(refresh);

        return ResponseEntity.ok()
                .body(resultDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refresh) {
        this.authService.logout(refresh);
        return ResponseEntity.ok()
                .build();
    }
}
