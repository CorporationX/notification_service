CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE telegram_account
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id   BIGINT  NOT NULL,
    chat_id   BIGINT  NOT NULL,
    confirmed BOOLEAN NOT NULL
);