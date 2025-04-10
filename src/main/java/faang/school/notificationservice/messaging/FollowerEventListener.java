package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.RedisChannel;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Слушатель событий подписки/отписки пользователей.
 * Реализует интерфейс MessageListener для обработки сообщений из Redis.
 * При получении сообщения извлекает событие, получает информацию о пользователе
 * и отправляет уведомление через соответствующий сервис.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RedisChannel("follower")
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final Executor asyncExecutor;
    private final MessageBuilder<FollowerEvent> messageBuilder;

    /**
     * Обрабатывает входящее сообщение из Redis.
     * Десериализует тело сообщения в объект FollowerEvent и запускает его обработку.
     *
     * @param message полученное сообщение
     * @param pattern шаблон канала, на который пришло сообщение
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            processEvent(event);
        } catch (IOException exception) {
            log.error("Deserialization error", exception);
        }
    }

    /**
     * Обрабатывает событие подписки/отписки.
     * Асинхронно получает информацию о пользователе и, если пользователь существует,
     * отправляет ему уведомление о событии.
     *
     * @param event событие подписки/отписки
     */
    private void processEvent(FollowerEvent event) {
        CompletableFuture.supplyAsync(() -> userServiceClient.getUser(event.getFolloweeId()), asyncExecutor)
                .thenAccept(user -> {
                    if (user != null) {
                        sendNotification(user, event);
                    }
                });
    }

    /**
     * Отправляет уведомление пользователю в соответствии с его предпочтениями.
     * Формирует сообщение на основе события и отправляет его через сервис уведомлений,
     * соответствующий предпочтениям пользователя.
     *
     * @param user  информация о пользователе
     * @param event событие подписки/отписки
     */
    private void sendNotification(UserDto user, FollowerEvent event) {
        Locale locale = Locale.forLanguageTag(user.getLanguage() != null ? user.getLanguage() : "en");

        String message = messageBuilder.buildMessage(event, locale);
        notificationServices.stream()
                .filter(s -> s.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresentOrElse(
                        s -> s.send(user, message),
                        () -> log.warn("No service for preference {}", user.getPreference()));
    }
}