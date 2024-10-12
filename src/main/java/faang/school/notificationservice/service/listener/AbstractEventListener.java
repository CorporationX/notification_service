package faang.school.notificationservice.service.listener;

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
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service found for user's preferred communication method"))
                .send(user, message);
    }

    protected String getMessage(T event, Locale userLocal) {
        return messageBuilders.stream()
                .filter(messageBuilders -> messageBuilders.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilders -> messageBuilders.buildMessage(event, userLocal))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No message builder found for given event type: " + event.getClass().getName()));
    }
}
