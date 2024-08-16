package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author Evgenii Malkov
 */
@Component
@RequiredArgsConstructor
public class  AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected T handleMessage(Message message, byte[] pattern, Class<T> clazz) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed parse message for class: " + clazz.getName(), e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter((messageBuilder) -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found messageBuilder for type: " + event.getClass()))
                .buildMessage(event, locale);
    }

    protected void sendNotification(UserDto user, String message) {
        notificationServices.stream()
                .filter((notificationService) -> notificationService.getPreferredContact() == user.getPreference())
                .forEach((service) -> service.send(user, message));
    }
}
