package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Not found any message builder for the event type: " + event.getClass().getName())
                ).buildMessage(event, userLocale);
    }

    protected void sendNotification(UserDto user, String message) {
        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Not found any notification service for user's preferred contact: " + user.getPreference())
                ).send(user, message);
    }
}
