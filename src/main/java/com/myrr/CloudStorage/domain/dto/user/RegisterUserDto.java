package com.myrr.CloudStorage.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dto для регистрации пользователя")
public record RegisterUserDto(
        @JsonProperty(value = "name")
        @NotNull
        @Size(min = 3, max = 55)
        @Schema(description = "Логин пользователя", example = "myrr")
        String name,

        @JsonProperty(value = "email")
        @NotNull
        @Size(min = 3, max = 55)
        @Email
        @Schema(description = "Email пользователя", example = "myrr@mail.org")
        String email,

        @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
        @Size(min = 8, max = 55)
        @NotNull
        @Schema(description = "Пароль пользователя", example = "12345678")
        String password
) {
}
