package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EventHandlingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (Exception e) {
            throw new EventHandlingException("Ошибка при обработке ивента", e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        MessageBuilder<T> builder = messageBuilders.stream()
                .filter(messageBuilder
                        -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Нет MessageBuilder для типа ивента: "
                        + event.getClass().getSimpleName()));
        log.info("Выбран MessageBuilder: {}", builder.getClass().getSimpleName());
        return builder.buildMessage(event, locale);
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        NotificationService service = notificationServices.stream()
                .filter(notificationService
                        -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Нет NotificationService для предпочтения: "
                        + user.getPreference()));
        log.info("Выбран NotificationService: {}", service.getPreferredContact());
        service.send(user, message);
    }
}
