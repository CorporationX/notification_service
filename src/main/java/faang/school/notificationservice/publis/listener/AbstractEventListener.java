package faang.school.notificationservice.publis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No matched event type or no message builder found for this event type"));
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matched preference contact"))
                .send(user, message);
    }
}
