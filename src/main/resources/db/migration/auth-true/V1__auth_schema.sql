-- Optional auth module. Only applied when APP_AUTH_ENABLED=true.
CREATE TABLE roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role_id    UUID         NOT NULL REFERENCES roles (id),
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- One row per active refresh token; only the SHA-256 of the token is stored.
CREATE TABLE sessions
(
    id                 UUID PRIMARY KEY,
    refresh_token_hash VARCHAR(64) NOT NULL UNIQUE,
    user_id            UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_user_id ON sessions (user_id);

-- Keep in sync with RoleEnum.
INSERT INTO roles (id, name)
VALUES ('00000000-0000-0000-0000-000000000001', 'ADMIN'),
       ('00000000-0000-0000-0000-000000000002', 'USER');
