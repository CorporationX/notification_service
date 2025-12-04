package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.HandleEventException;
import faang.school.notificationservice.messaging.builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;
    private final Class<T> eventType;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Received event: {}", event);
            consumer.accept(event);

        } catch (IOException e) {
            log.error("Failed to process message: {}", message, e);
            throw new HandleEventException("Failed to process message: " + message, e);
        }
    }

    protected String getMessage(T event, Locale userLocal) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType().isAssignableFrom(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocal))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the event type: "
                        + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact()
                        .equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no preferred contact method found for event {}"))
                .send(user, message);
        log.info("Notification sent to user: {}", id);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, eventType, this::processEvent);
    }

    protected abstract void processEvent(T event);
}