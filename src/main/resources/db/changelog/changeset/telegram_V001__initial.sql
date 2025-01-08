CREATE TABLE telegram_chat (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    chat_id bigint NOT NULL,
    user_id bigint NOT NULL,

    UNIQUE (chat_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);