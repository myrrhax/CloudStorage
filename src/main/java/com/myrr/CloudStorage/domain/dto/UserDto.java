package com.myrr.CloudStorage.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.myrr.CloudStorage.utils.jsonmarkers.PrivateView;
import com.myrr.CloudStorage.utils.jsonmarkers.PublicView;
import com.myrr.CloudStorage.utils.validation.Login;
import com.myrr.CloudStorage.utils.validation.OnCreate;
import com.myrr.CloudStorage.utils.validation.OnUpdate;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.hibernate.validator.constraints.URL;

import java.util.Set;
import java.util.UUID;

public record UserDto (
    @JsonProperty(value = "id")
    @NotNull(groups = { OnUpdate.class })
    @JsonView(PublicView.class)
    Long id,

    @JsonProperty(value = "name")
    @NotNull(groups = {Default.class, Login.class})
    @Size(min = 3, max = 55)
    @JsonView(PublicView.class)
    String name,

    @JsonProperty(value = "email")
    @JsonView(PrivateView.class)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3, max = 55)
    @Email
    String email,

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 55, groups = { OnCreate.class })
    @NotNull(groups = {OnCreate.class, Login.class})
    @JsonView(PublicView.class)
    String password,

    @JsonProperty(value = "roles", access = JsonProperty.Access.READ_ONLY)
    @JsonView(PublicView.class)
    Set<String> roles,

    @JsonProperty(value = "is_confirmed", access = JsonProperty.Access.READ_ONLY)
    @JsonView(PrivateView.class)
    Boolean isConfirmed,

    @JsonProperty("avatar_url")
    @URL
    @Null
    @JsonView(PublicView.class)
    String avatarUrl
) {
}
