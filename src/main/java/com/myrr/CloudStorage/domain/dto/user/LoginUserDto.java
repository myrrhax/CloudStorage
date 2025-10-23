package com.myrr.CloudStorage.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dto для логина")
public record LoginUserDto(
        @JsonProperty(value = "name")
        @NotNull
        @Size(min = 3, max = 55)
        @Schema(description = "Логин пользователя", example = "myrr")
        String name,

        @JsonProperty(value = "password")
        @Size(min = 8, max = 55)
        @NotNull
        @Schema(description = "Пароль пользователя", example = "12345678")
        String password
) {
}
