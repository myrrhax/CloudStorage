package com.myrr.CloudStorage.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.service.UserService;
import com.myrr.CloudStorage.utils.jsonmarkers.PrivateView;
import com.myrr.CloudStorage.utils.jsonmarkers.PublicView;
import com.myrr.CloudStorage.utils.validation.OnCreate;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @JsonView(PrivateView.class)
    public ResponseEntity<UserDto> register(@RequestBody @Validated({Default.class, OnCreate.class}) UserDto dto,
                                        UriComponentsBuilder uriComponentsBuilder) {
        UserDto resultDto = this.userService.addUser(dto);
        UriComponents location = uriComponentsBuilder
                .path("/api/auth/users/{id}")
                .buildAndExpand(resultDto.id());

        return ResponseEntity.created(location.toUri())
                .body(resultDto);
    }

    @GetMapping("/users/{id}")
    @JsonView(PublicView.class)
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto dto = this.userService.getUser(id);

        return ResponseEntity.ok(dto);
    }
}
