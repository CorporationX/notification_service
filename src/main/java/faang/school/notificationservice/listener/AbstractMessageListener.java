package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Абстрактный класс для реализации общей логики слушателей (подписчиков) топиков
 *
 * @author Linempy
 * @since 14.08.2025
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userClient;
    protected final List<NotificationService> services;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void handleMessage(Message message, Class<T> type, Consumer<T> handler) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            handler.accept(event);
        } catch (IOException e) {
            log.error("Ошибка в десереализации");
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Билдер сообщений не найден для типа ивента: " + event.getClass().getName())
                );
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    protected void sendMessage(Long id, String text) {
        UserDto user = userClient.getUser(id);
        log.debug("Получил пользователя id={} из сервиса userService", user.getId());
        services.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(user, text));
    }
}