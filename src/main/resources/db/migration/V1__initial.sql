CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(55) NOT NULL UNIQUE,
    display_name    VARCHAR(55) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    is_confirmed    BOOLEAN NOT NULL DEFAULT false,
    avatar_url      VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(55) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id     BIGINT NOT NULL,
    role_id     INTEGER NOT NULL,

    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE NO ACTION
);