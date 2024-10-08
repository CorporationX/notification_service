CREATE TABLE telegram_user (
    id BIGINT PRIMARY KEY,                -- Telegram User ID
    user_name VARCHAR(255),               -- Telegram аккаунт
    first_name VARCHAR(255),              -- Имя пользователя
    last_name VARCHAR(255),               -- Фамилия пользователя
    user_id BIGINT NULL,                  -- ID пользователя из таблицы users, может быть NULL
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Внешний ключ на таблицу users
);
