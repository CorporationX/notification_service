package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<T> messageBuilder;
    private final List<NotificationService> notificationServices;

    public T handleEvent(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error("Error during reading message from redis topic: ", e);
            throw new RuntimeException(e);
        }
    }

    public String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification type was not found in existing options"))
                .send(user, message);
        log.info("Notification was sent to user with id {}", userId);
    }
}