package faang.school.notificationservice.eventlistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event =  objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error deserializing message: {}. Message text: {}",
                    e.getMessage(), message.getBody());
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("Mo message builder found for the given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mo notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}
