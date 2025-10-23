package com.myrr.CloudStorage.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.myrr.CloudStorage.utils.jsonmarkers.PrivateView;
import com.myrr.CloudStorage.utils.jsonmarkers.PublicView;

import java.util.Set;

public record UserDto (
    @JsonProperty(value = "id")
    @JsonView(PublicView.class)
    Long id,

    @JsonProperty(value = "name")
    @JsonView(PublicView.class)
    String name,

    @JsonProperty(value = "email")
    @JsonView(PrivateView.class)
    String email,

    @JsonProperty(value = "roles")
    @JsonView(PublicView.class)
    Set<String> roles,

    @JsonProperty(value = "isConfirmed")
    @JsonView(PrivateView.class)
    Boolean isConfirmed,

    @JsonProperty("avatarUrl")
    @JsonView(PublicView.class)
    String avatarUrl
) {
}
