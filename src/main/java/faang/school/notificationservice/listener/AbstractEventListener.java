package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
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

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(builder -> builder.supportsEventType() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        log.info("User actor: {}, {}", user.getUsername(), user.getEmail());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}
