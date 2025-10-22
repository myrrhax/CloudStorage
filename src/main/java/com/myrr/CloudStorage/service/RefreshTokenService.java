package com.myrr.CloudStorage.service;

import com.myrr.CloudStorage.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> getToken(String token);

    void delete(RefreshToken token);
}
