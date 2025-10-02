package com.myrr.CloudStorage.controller;

import com.myrr.CloudStorage.domain.dto.UserDto;
import com.myrr.CloudStorage.security.JwtEntity;
import com.myrr.CloudStorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal UserDetails userDetails) {
        JwtEntity providedDetails = (JwtEntity) userDetails;

        return ResponseEntity.ok(this.userService.getUser(providedDetails.getId()));
    }
}
