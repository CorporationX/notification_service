package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.NotificationData;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final List<NotificationService> notificationServices;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessageBody(Message message) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        log.info("Received message from channel '{}': {}", channel, messageBody);
        return messageBody;
    }

    protected T getEvent(String messageBody, Class<T> type) {
        try {
            return (T) objectMapper.readValue(messageBody, type);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for {}", type.getName(), e);
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale, NotificationData data) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(data, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder for: " + event.getClass().getName()));
    }

    protected void sendNotification(UserDto user, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service for user preference. Id: " + user.getId()))
                .send(user, message);
        log.info("Message sent to user Id: {}, by {}", user.getId(), user.getPreference());
    }
}
