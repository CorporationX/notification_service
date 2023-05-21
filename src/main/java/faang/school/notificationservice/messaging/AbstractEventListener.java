package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.service.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<?>> messageBuilders;

    protected String getMessage(Class<?> eventType, Locale locale, String... args) {
        return messageBuilders.stream()
                .filter(builder -> builder.getEvent() == eventType)
                .findFirst()
                .map(builder -> builder.getMessage(locale, args))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for event: " + eventType));
    }

    protected void sendNotification(long userId, String message) {
        var user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresentOrElse(
                        service -> service.send(user, message),
                        () -> log.warn("No notification service found for user: {} and preference: {}", user, user.getPreference())
                );
    }
}
