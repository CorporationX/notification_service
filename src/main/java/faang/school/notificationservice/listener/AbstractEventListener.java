package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<?>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected AbstractEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<MessageBuilder<?>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
        this.messageBuilders = messageBuilders;
        this.notificationServices = notificationServices;
    }

    protected abstract Class<T> getEventClass();

    protected abstract void handleEvent(T event);

    protected String getMessage(T event, Locale locale, Object... placeholders) {
        log.info("Building message for event: {}", event);
        return messageBuilders.stream()
                .filter(builder -> builder.isEventTypeSupported(event))
                .findFirst()
                .map(builder -> (MessageBuilder<T>) builder)
                .map(builder -> builder.buildMessage(event, locale, placeholders))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long userId, String message) {
        log.info("Sending notification to userId: {} with message: {}", userId, message);

        UserDto user = userServiceClient.getUser(userId);

        log.info("Fetched UserDto: {}", user);
        log.info("User's preferred contact method: {}", user.getPreference());

        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for user's preferred contact method: " + user.getPreference()))
                .send(user, message);
    }
}
