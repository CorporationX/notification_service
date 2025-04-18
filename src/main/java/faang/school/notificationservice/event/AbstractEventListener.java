package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EventListenerException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Базовый класс для слушателей событий, обрабатывающих сообщения из Redis.
 * <p>
 * Предоставляет методы для получения локали пользователя, предпочтительного способа связи,
 * обработки событий и отправки уведомлений.
 * </p>
 *
 * @param <T> Тип события
 */
@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    /**
     * Получает локаль для пользователя
     * @param userId ID пользователя
     * @return Locale из профиля пользователя или ENGLISH по умолчанию
     */
    protected Locale getUserLocale(Long userId) {
        try {
            UserDto user = userServiceClient.getUser(userId);
            return user != null && user.getLocale() != null
                    ? user.getLocale()
                    : Locale.ENGLISH;
        } catch (Exception e) {
            log.warn("Failed to get locale for user {}, using default. Error: {}",
                    userId, e.getMessage());
            return Locale.ENGLISH;
        }
    }

    /**
     * Получает предпочтительный способ связи с пользователем
     * @param userId ID пользователя
     * @return PreferredContact из профиля пользователя или EMAIL по умолчанию
     */
    protected UserDto.PreferredContact getPreferredContact(Long userId) {
        try {
            UserDto user = userServiceClient.getUser(userId);
            return user != null && user.getPreference() != null
                    ? user.getPreference()
                    : UserDto.PreferredContact.EMAIL;
        } catch (Exception e) {
            log.warn("Failed to get contact preference for user {}, using default. Error: {}",
                    userId, e.getMessage());
            return UserDto.PreferredContact.EMAIL;
        }
    }


    /**
     * Считывает тело событие и отправляет в обработку
     *
     * @param message  Сообщение из Redis
     * @param clazz    Class объекта
     * @param consumer Обработчик события
     */
    protected void handleEvent(@NotNull Message message, @NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            log.debug("Event received: {}", event);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error processing event: {}", message, e);
            throw new EventListenerException("Error processing event");
        }
    }

    /**
     * Получение отформатированного текста
     *
     * @param event  Объект для обработки текста
     * @param locale Локаль пользователя
     * @return Отформатированное сообщение
     */
    protected String getMessage(@NotNull T event, Locale locale) {
        Locale targetLocale = locale != null ? locale : Locale.ENGLISH;

        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, targetLocale))
                .orElseThrow(() -> {
                    log.error("No suitable builder found for {}", event.getClass());
                    String errorMsg = String.format("No suitable builder found for %s", event.getClass());
                    return new EventListenerException(errorMsg);
                });
    }

    /**
     * Отправляет сообщение
     *
     * @param userId  Id получателя
     * @param message Текст сообщения
     */
    protected void sendNotification(@NotNull Long userId, @NotBlank String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        if (userDto == null) {
            log.error("User with id {} not found", userId);
            String errorMsg = String.format("User with id %s not found", userId);
            throw new UserNotFoundException(errorMsg);
        }

        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Notification service for user preferred contact {} not found", userDto.getPreference());
                    String errorMsg = String.format("Notification service for user preferred contact %s not found",
                            userDto.getPreference());
                    return new EventListenerException(errorMsg);
                })
                .send(userDto, message);
    }
}