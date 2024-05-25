CREATE TABLE telegram_profiles (
    id        BIGSERIAL    PRIMARY KEY UNIQUE,
    user_id   BIGINT       NOT NULL,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    chat_id   BIGINT       NOT NULL UNIQUE,
)