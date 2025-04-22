package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType().isAssignableFrom(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> {
                    log.error("No message builder found for event type {}", event.getClass());
                    return new RuntimeException("No message builder found for event");
                });
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        log.info("Sending notification to user {} via {}", user.getId(), user.getPreference());

        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No NotificationService found for {}", user.getPreference());
                    return new RuntimeException("No notification service found for user's preferred contact method");
                })
                .send(user, message);
    }
}
