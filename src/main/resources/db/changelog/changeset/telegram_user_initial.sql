CREATE TABLE telegram_users (
    user_id bigint PRIMARY KEY REFERENCES users ON DELETE CASCADE,
    telegram_chat_id bigint NOT NULL  
);