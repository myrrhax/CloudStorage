CREATE TABLE IF NOT EXISTS t_refresh_tokens (
    id          UUID PRIMARY KEY,
    token       varchar(255) NOT NULL UNIQUE,
    issued_at   TIMESTAMP NOT NULL CHECK (issued_at <= now()),
    expires_at  TIMESTAMP NOT NULL CHECK (expires_at > now())
);

CREATE TABLE IF NOT EXISTS t_user_refresh_tokens (
    user_id     bigint references users(id),
    refresh_id  UUID references t_refresh_tokens(id),
    PRIMARY KEY(user_id, refresh_id)
);