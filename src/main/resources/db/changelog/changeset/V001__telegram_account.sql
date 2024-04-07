CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE IF NOT EXISTS TABLE telegram_account
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id   BIGINT UNIQUE NOT NULL,
    chat_id   BIGINT UNIQUE NOT NULL,
    confirmed BOOLEAN       NOT NULL
);