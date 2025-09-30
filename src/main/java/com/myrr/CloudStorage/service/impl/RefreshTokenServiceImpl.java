package com.myrr.CloudStorage.service.impl;

import com.myrr.CloudStorage.domain.entity.RefreshToken;
import com.myrr.CloudStorage.repository.RefreshTokenRepository;
import com.myrr.CloudStorage.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> getToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    @Override
    public void delete(RefreshToken token) {
        this.refreshTokenRepository.delete(token);
    }
}
