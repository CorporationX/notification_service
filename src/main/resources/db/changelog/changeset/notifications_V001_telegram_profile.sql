CREATE TABLE IF NOT EXISTS telegram_profile
(
    id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id   BIGINT                                        NOT NULL,
    user_name VARCHAR(255)                                  NOT NULL,
    chat_id   BIGINT                                        NOT NULL
);