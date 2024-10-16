create table telegram_contacts (
    id bigint primary key generated always as identity,
    username varchar(64) unique not null,
    chat_id bigint not null
);

comment on table telegram_contacts is 'Таблица с данными об имени пользователя и его chat_id с ботом в телеграмме';
comment on column telegram_contacts.id is 'Суррогатный ключ, генерируется в бд';
comment on column telegram_contacts.username is 'Имя пользователя в телеграмме, в формате @Username';
comment on column telegram_contacts.chat_id is 'Ид чата пользователя с ботом';