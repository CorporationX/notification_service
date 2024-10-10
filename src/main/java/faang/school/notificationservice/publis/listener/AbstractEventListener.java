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
        MessageBuilder<T> tMessageBuilder = messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), event.getClass()))
                .findFirst()
                .orElseThrow(() -> {
                    String e = String.format("No matched event type for: " + event.getClass());
                    log.error(e);
                    throw new IllegalArgumentException(e);
                });

        return tMessageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        log.info("Get User: " + user);

        NotificationService notificationService = notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    String e = String.format("No matched preference contact for: " + user.getPreference());
                    log.error(e);
                    throw new IllegalArgumentException(e);
                });

        notificationService.send(user, message);
    }
}
