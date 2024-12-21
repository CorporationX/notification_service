package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the provided event " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreferredContact())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for the user's preferred communication method"))
                .send(user, message);
    }

    protected void handleEvent(Class<T> type, Message message, Consumer<T> consumer) {
        if (message.getBody() == null) {
            log.error("Received no message to process");
            return;
        }

        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Received an event from Redis channel: {}", event.getClass().getName());
            consumer.accept(event);
        } catch (Exception e) {
            log.error("Failed to send a message", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
