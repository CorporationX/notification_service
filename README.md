# Service Template

Стандартный шаблон проекта на SpringBoot

# Использованные технологии

* [Spring Boot](https://spring.io/projects/spring-boot) – как основной фрэймворк
* [PostgreSQL](https://www.postgresql.org/) – как основная реляционная база данных
* [Redis](https://redis.io/) – как кэш и очередь сообщений через pub/sub
* [testcontainers](https://testcontainers.com/) – для изолированного тестирования с базой данных
* [Liquibase](https://www.liquibase.org/) – для ведения миграций схемы БД
* [Gradle](https://gradle.org/) – как система сборки приложения
* [Lombok](https://projectlombok.org/) – для удобной работы с POJO классами
* [MapStruct](https://mapstruct.org/) – для удобного маппинга между POJO классами

# База данных

* База поднимается в отдельном сервисе [infra](../infra)
* Redis поднимается в единственном инстансе тоже в [infra](../infra)
* Liquibase сам накатывает нужные миграции на голый PostgreSql при старте приложения
* В тестах используется [testcontainers](https://testcontainers.com/), в котором тоже запускается отдельный инстанс
  postgres
* В коде продемонстрирована работа как с JdbcTemplate, так и с JPA (Hibernate)

# Как начать разработку начиная с шаблона?

1. Сначала нужно склонировать этот репозиторий

```shell
git clone https://github.com/FAANG-School/ServiceTemplate
```

2. Далее удаляем служебную директорию для git

```shell
# Переходим в корневую директорию проекта
cd ServiceTemplate
rm -rf .git
```

3. Далее нужно создать совершенно пустой репозиторий в github/gitlab

4. Создаём новый репозиторий локально и коммитим изменения

```shell
git init
git remote add origin <link_to_repo>
git add .
git commit -m "<msg>"
```

Готово, можно начинать работу!

# Как запустить локально?

Сначала нужно развернуть базу данных из директории [infra](../infra)

Далее собрать gradle проект

```shell
# Нужно запустить из корневой директории, где лежит build.gradle.kts
gradle build
```

Запустить jar'ник

```shell
java -jar build/libs/ServiceTemplate-1.0.jar
```

Но легче всё это делать через IDE

# Код

RESTful приложения калькулятор с единственным endpoint'ом, который принимает 2 числа и выдает результаты их сложения,
вычитаяни, умножения и деления

* Обычная трёхслойная
  архитектура – [Controller](src/main/java/faang/school/notificationservice/controller), [Service](src/main/java/faang/school/notificationservice/service), [Repository](src/main/java/faang/school/notificationservice/repository)
* Слой Repository реализован и на jdbcTemplate, и на JPA (Hibernate)
* Написан [GlobalExceptionHandler](src/main/java/faang/school/notificationservice/controller/GlobalExceptionHandler.java)
  который умеет возвращать ошибки в формате `{"code":"CODE", "message": "message"}`
* Используется TTL кэширование вычислений
  в [CalculationTtlCacheService](src/main/java/faang/school/notificationservice/service/cache/CalculationTtlCacheService.java)
* Реализован простой Messaging через [Redis pub/sub](https://redis.io/docs/manual/pubsub/)
  * [Конфигурация](src/main/java/faang/school/notificationservice/config/RedisConfig.java) –
    сетапится [RedisTemplate](https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/RedisTemplate.html) –
    класс, для удобной работы с Redis силами Spring
  * [Отправитель](src/main/java/faang/school/notificationservice/service/messaging/RedisCalculationPublisher.java) – генерит
    рандомные запросы и отправляет в очередь
  * [Получатель](src/main/java/faang/school/notificationservice/service/messaging/RedisCalculationSubscriber.java) –
    получает запросы и отправляет задачи асинхронно выполняться
    в [воркер](src/main/java/faang/school/notificationservice/service/worker/CalculationWorker.java)

# Тесты

Написаны только для единственного REST endpoint'а
* SpringBootTest
* MockMvc
* Testcontainers
* AssertJ
* JUnit5
* Parameterized tests

# TODO

* Dockerfile, который подключается к сети запущенной postgres в docker-compose
* Redis connectivity
* ...

# Telegram Notification Service

## Текущая реализация
Сервис отправляет сообщения пользователям через Telegram Bot API, но имеет **критическое ограничение**:
- Бот может писать только тем пользователям, которые **уже начали с ним диалог** (отправили `/start`).

## Проблема
Метод `send()` использует `user.getId()` как `chatId`, что:
1. Не будет работать, если `user.getId()` ≠ Telegram `chat_id`.
2. Требует предварительной активации чата пользователем.

## Предлагаемое решение
### 1. Механизм подписки
Реализовать два компонента:
1. **Эндпоинт для генерации подписочной ссылки**
   ```
   GET /telegram/subscribe/{userId} → Возвращает ссылку вида `https://t.me/YourBot?start=TOKEN`
   ```
2. **Обработчик команды /start в боте**  
   Связывает `TOKEN` → `user_id` → `chat_id` и сохраняет в БД.

### 2. Обновлённый workflow
1. Пользователь в приложении нажимает "Подписаться":
   ```mermaid
   sequenceDiagram
       User->>Backend: GET /telegram/subscribe/123
       Backend->>User: https://t.me/YourBot?start=ABC123
       User->>Telegram: Отправляет /start ABC123
       Telegram->>Bot: Передаёт chat_id и токен
       Bot->>Backend: Сохраняет chat_id для user_id=123
   ```
2. После активации бот может отправлять уведомления.

## Как доработать?
1. Добавить таблицу `user_telegram_data`:
   ```sql
   CREATE TABLE user_telegram_data (
       user_id BIGINT PRIMARY KEY,
       telegram_chat_id BIGINT,
       is_active BOOLEAN
   );
   ```
2. Реализовать:
  - `TelegramSubscriptionController` для генерации ссылок
  - `TelegramBotUpdatesListener` для обработки `/start`

## Важно!
Текущий код класса `TelegramService` — это **заготовка**, работающая только для пользователей, уже написавших боту. 
Для полной функциональности требуется реализовать механизм подписки.
