CREATE TABLE IF NOT EXISTS t_refresh_tokens (
    id          UUID PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    issued_at   TIMESTAMP NOT NULL,
    expires_at  TIMESTAMP NOT NULL
);