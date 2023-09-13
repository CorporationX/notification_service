CREATE TABLE IF NOT EXISTS telegram_profile
(
        id BIGSERIAL PRIMARY KEY,
        user_name VARCHAR(255),
        user_id BIGINT NOT NULL,
        chat_id BIGINT NOT NULL
);