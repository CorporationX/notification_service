CREATE TABLE if not exists users (
   id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
   username varchar(64) UNIQUE NOT NULL,
   password varchar(128) NOT NULL,
   email varchar(64) UNIQUE NOT NULL,
   phone varchar(32) UNIQUE,
   about_me varchar(4096),
   active boolean DEFAULT true NOT NULL,
   city varchar(64),
   country_id bigint NOT NULL,
   experience int,
   created_at timestamptz DEFAULT current_timestamp,
   updated_at timestamptz DEFAULT current_timestamp,

   CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES country (id)
);

ALTER TABLE users
ADD COLUMN if not exists telegram_login varchar(255),
ADD COLUMN if not exists telegram_chat_id bigint;