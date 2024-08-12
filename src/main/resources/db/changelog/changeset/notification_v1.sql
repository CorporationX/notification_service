CREATE TABLE tg_chats (
                          id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                          user_id BIGINT NOT NULL,
                          chat_id BIGINT NOT NULL
);