package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(
        Message message,
        Class<T> eventType,
        Consumer<T> consumer
    ) {
        try {
            var event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            String errorMessage = "Failed to process event of type %s. Error details: %s"
                .formatted(eventType, e.getMessage());
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    protected String getMessage(
        T event,
        Locale locale
    ) {
        return messageBuilders.stream()
            .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
            .findFirst()
            .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
            .orElseThrow(() -> new IllegalArgumentException("No message builder found for event type: %s"
                .formatted(event.getClass().getName())));
    }

    protected void sendNotification(
        long userId,
        String message
    ) {
        var userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
            .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No notification service found for preference: %s"
                .formatted(userDto.getPreference())))
            .send(userDto, message);
    }
}
