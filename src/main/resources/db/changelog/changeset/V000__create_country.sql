CREATE TABLE if not exists country (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    title varchar(64) UNIQUE NOT NULL
);