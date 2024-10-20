package faang.school.notificationservice.publis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected T mapToEvent(String messageBody, Class<T> eventType) {
        try {
            return objectMapper.readValue(messageBody, eventType);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        List<MessageBuilder<T>> eventMessageBuilders = messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), event.getClass()))
                .toList();

        messageBuilderSizeValidation(event, eventMessageBuilders);

        MessageBuilder<T> eventMessageBuilder = eventMessageBuilders.get(0);
        return eventMessageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        log.info("Get User: " + user);

        List<NotificationService> eventNotificationServices = notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .toList();

        notificationServiceSizeValidation(user, eventNotificationServices);

        NotificationService notificationService = eventNotificationServices.get(0);
        notificationService.send(user, message);
    }

    private void messageBuilderSizeValidation(T event, List<MessageBuilder<T>> eventMessageBuilders) {
        if (eventMessageBuilders.size() > 1) {
            String e = String.format("Several matched event types for: " + event.getClass());
            log.error(e, eventMessageBuilders);
            throw new IllegalArgumentException(e);
        } else if (eventMessageBuilders.isEmpty()) {
            String e = String.format("No matched event type for: " + event.getClass());
            log.error(e);
            throw new IllegalArgumentException(e);
        }
    }

    private void notificationServiceSizeValidation(UserDto user, List<NotificationService> eventNotificationServices) {
        if (eventNotificationServices.size() > 1) {
            String e = String.format("Several matched preference contact for: " + user.getPreference());
            log.error(e, eventNotificationServices);
            throw new IllegalArgumentException(e);
        } else if (eventNotificationServices.isEmpty()) {
            String e = String.format("No matched preference contact for: " + user.getPreference());
            log.error(e);
            throw new IllegalArgumentException(e);
        }
    }
}
