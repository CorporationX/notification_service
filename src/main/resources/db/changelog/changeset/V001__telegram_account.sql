CREATE TABLE telegram_account
(
    id      bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    chat_id bigint NOT NULL
);