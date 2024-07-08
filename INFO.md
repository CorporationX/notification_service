# Информация по настройке интеграций с внешними сервисами.

# Telegram

## Регистрация бота Телеграмм и формирование токена

1. В приложении Telegram (сайт или мобильное приложение) ищем [@BotFather](https://telegram.me/botfather).
    
2. Данному боту пишем команду ***/start***. 
Отобразится список команд, в котором будет присутствовать ***/newbot*** (создание нового бота)
3. Вводим ***/newbot***
4. Будет предложено ввести имя бота. Вводим любое смысловое название.
5. Далее будет предложено ввести ***username*** (любое уникальное имя оканчивающееся на bot).
Имя пользователя, которое будет использоваться для формирования ссылки на бот.
6. После успешной регистрации бота отобразится токен, который вставляем 
   в application.yaml нашего приложения и ссылка на бот.

```
telegram:
  bot:
    token: "XXXXXXXX:DKJR5878HDHJEHHUHUHUdffdjjjjj$#&fjkk"
```

## Получение chatId
1. Переходим в чат бота и нажимаем кнопку ***"Запустить"***
2. Пишем что-нибудь созданному боту (необходимо для того, чтобы бот выдал в последствии лог)
3. Открываем браузер и вводим URL: **`https://api.telegram.org/bot<Токен созданного бота>/getUpdates`**
4. В логе ищем текст вида:

```antlrv4
"message": {
        "message_id": 1,
        "from": {
          "id": 296131911,
```

"id": 296131911 - ид чата.