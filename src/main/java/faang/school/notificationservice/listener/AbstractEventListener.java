package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;

    public abstract void onMessage(T event);

    protected void handleMessage(T event, long userId) {
        UserDto user = userServiceClient.getUser(userId);
        String message = getMessage(event, user.getLocale());
        sendNotification(user.getId(), message);
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type" +
                        event.getClass().getName()));
    }

    @SneakyThrows
    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for " +
                        user.getPreference().toString()))
                .send(user, message);
    }
}
