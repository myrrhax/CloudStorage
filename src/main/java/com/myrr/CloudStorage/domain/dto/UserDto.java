package com.myrr.CloudStorage.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.myrr.CloudStorage.utils.jsonmarkers.PrivateView;
import com.myrr.CloudStorage.utils.validation.OnCreate;
import com.myrr.CloudStorage.utils.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

public record UserDto (
    @JsonProperty("id")
    @NotBlank(groups = { OnUpdate.class })
    Long id,

    @JsonProperty("name")
    @Size(min = 3, max = 55)
    String name,

    @JsonProperty("email")
    @JsonView(PrivateView.class)
    @Size(min = 3, max = 55)
    @Email
    String email,

    @JsonProperty(value = "password", access = JsonProperty.Access.READ_ONLY)
    @Size(min = 8, max = 55, groups = { OnCreate.class })
    String password,

    @JsonProperty(value = "roles", access = JsonProperty.Access.READ_ONLY)
    Set<String> roles,

    @JsonProperty(value = "is_confirmed", access = JsonProperty.Access.WRITE_ONLY)
    @JsonView(PrivateView.class)
    Boolean isConfirmed,

    @JsonProperty("avatar_url")
    @URL
    String avatarUrl
) {

}
