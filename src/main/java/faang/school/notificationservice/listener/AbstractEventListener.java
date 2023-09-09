package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.dto.notification.NotificationData;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T, V> {
    protected final ObjectMapper objectMapper;
    protected final List<NotificationService> notificationServices;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T, V>> messageBuilders;

    public String getMessage(Class<?> eventType, Locale locale, T event, V argument) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getEventType().equals(eventType))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale, argument))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .send(userDto, message);
    }

    public String getMessageBody(Message message) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        log.info("Received message from channel '{}': {}", channel, messageBody);
        return messageBody;
    }

    public T getEvent(String messageBody, Class<T> type) {
        try {
            return (T) objectMapper.readValue(messageBody, type);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for {}", type.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
