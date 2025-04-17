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
 * Абстрактный слушатель обработки всех входящих событий
 * <p>
 * Слушатель передает на обработку события сервисам
 * </p>
 *
 * <p>
 * Основные функции:
 * <ul>
 *      <li>{@link #handleEvent(Message, Class, Consumer)} Считывает тело события и отправляет в обработку.</li>
 *      <li>{@link #getMessage(Object, Locale)} Преобразует тест в зависимости от класса объекта и локали.</li>
 *      <li>{@link #sendNotification(Long, String)}  Отправляет нотификацию указанному юзеру.</li>
 * </ul>
 * </p>
 *
 * @author takewqa
 * @see Message
 * @see Locale
 * @see Consumer
 * @see ObjectMapper
 * @see UserServiceClient
 * @see MessageBuilder
 * @see NotificationService
 */
@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

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
            throw new EventListenerException(e);
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
        if(locale == null) {
            locale = Locale.getDefault();
        }
        Locale finalLocale = locale;
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, finalLocale))
                .orElseThrow(() -> {
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
            String errorMsg = String.format("User with id %s not found", userId);
            throw new UserNotFoundException(errorMsg);
        }

        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMsg = String.format("Notification service for user preferred contact %s not found",
                            userDto.getPreference());
                    return new EventListenerException(errorMsg);
                })
                .send(userDto, message);
    }
}
