package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .ifPresentOrElse(service -> service.send(user, message), () -> {
                    throw new IllegalStateException("No notification service found for " + user.getPreference());
                });
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getEventType() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalStateException("No message builder found for event type: " + event.getClass()));
    }
}
