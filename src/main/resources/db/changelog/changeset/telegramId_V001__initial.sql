CREATE TABLE telegram_id
(
    id      bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id BIGINT NOT NULL UNIQUE,
    chat_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);
