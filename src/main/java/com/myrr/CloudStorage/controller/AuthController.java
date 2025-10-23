package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.TokenResponseDto;
import com.myrr.CloudStorage.domain.dto.user.LoginUserDto;
import com.myrr.CloudStorage.domain.dto.user.RegisterUserDto;
import com.myrr.CloudStorage.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Validated
@Tag(name = "Аутентификация", description = "Контроллер с методами для работы с аккаунтом")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация аккаунта", description = "Позволяет зарегистрировать новый аккаунт")
    public ResponseEntity<TokenResponseDto> register(@RequestBody RegisterUserDto dto,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        TokenResponseDto resultDto = this.authService.register(dto);
        UriComponents location = uriComponentsBuilder
                .path("/api/auth") // ToDo change
                .buildAndExpand(resultDto.user().id());

        return ResponseEntity.created(location.toUri())
                .body(resultDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в аккаунт", description = "Позволяет войти в аккаунт и получить токены доступа")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginUserDto dto) {
        TokenResponseDto resultDto = this.authService.login(dto);

        return ResponseEntity.ok()
                .body(resultDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновление токенов", description = "Позволяет получить новую пару токенов по refresh-токену")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Parameter(name = "Токен обновления", required = true) final String refresh) {
        TokenResponseDto resultDto = this.authService.refresh(refresh);

        return ResponseEntity.ok()
                .body(resultDto);
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход из аккаунта", description = "Позволяет инвалидировать токен")
    public ResponseEntity<Void> logout(@RequestBody String refresh) {
        this.authService.logout(refresh);
        return ResponseEntity.ok()
                .build();
    }
}
