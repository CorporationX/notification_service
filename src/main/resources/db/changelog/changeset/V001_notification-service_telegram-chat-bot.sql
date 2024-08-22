create table telegram_chat_bot(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    chat_id bigint NOT NULL
    );
CREATE INDEX idx_user_id ON telegram_chat_bot(user_id);