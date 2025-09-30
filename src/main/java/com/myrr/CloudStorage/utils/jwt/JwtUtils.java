package com.myrr.CloudStorage.utils.jwt;

import com.myrr.CloudStorage.domain.entity.RefreshToken;
import com.myrr.CloudStorage.domain.entity.User;
import com.myrr.CloudStorage.domain.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "jwt")
public final class JwtUtils {
    public static final String NAME_CLAIM_NAME = "name";
    public static final String ROLES_CLAIM_NAME = "roles";
    private String secret;
    private Duration lifetime;
    private Duration refreshLifetime;
    private String issuer;
    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = Map.of(
            ROLES_CLAIM_NAME, user.getRoles()
                        .stream()
                        .map(role -> role.getRole().name())
                        .toList()
        );
        Instant issuedTime = Instant.now();
        Instant expirationTime = issuedTime.plus(lifetime);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getName())
                .issuedAt(Date.from(issuedTime))
                .expiration(Date.from(expirationTime))
                .issuer(issuer)
                .signWith(key)
                .compact();
    }

    public RefreshToken generateRefreshToken(User user) {
        List<String> authorities = new LinkedList<>();
        authorities.add("REFRESH");
        authorities.add("LOGOUT");
        user.getRoles()
                .stream()
                .map(role -> role.getRole().name())
                .map(role -> "GRANT_" + role)
                .forEach(authorities::add);
        Map<String, Object> claims = Map.of(
            ROLES_CLAIM_NAME, authorities
        );
        Instant issuedTime = Instant.now();
        Instant expirationTime = issuedTime.plus(refreshLifetime);

        String generatedToken = Jwts.builder()
                .claims(claims)
                .subject(user.getName())
                .issuedAt(Date.from(issuedTime))
                .expiration(Date.from(expirationTime))
                .issuer(issuer)
                .signWith(key)
                .compact();
        return new RefreshToken(generatedToken, issuedTime, expirationTime, user);
    }

    public String getUsername(String token) {
        return parseClaims(token)
                .getSubject();
    }

    public List<String> getRoles(String token) {
        List claims = parseClaims(token)
                .get(ROLES_CLAIM_NAME, List.class);
        if (claims == null) {
            throw new IllegalArgumentException("Role claims cannot be empty");
        }
        return claims.stream()
                .map(Object::toString)
                .toList();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getLifetime() {
        return lifetime;
    }

    public void setLifetime(Duration lifetime) {
        this.lifetime = lifetime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setRefreshLifetime(Duration refreshLifetime) {
        this.refreshLifetime = refreshLifetime;
    }
}
