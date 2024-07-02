CREATE TABLE telegram_chat (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    chat_id bigint NOT NULL,
    post_author_id bigint NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (post_author_id) REFERENCES users (id)
);