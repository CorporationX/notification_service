package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final MessageBuilder<T> messageBuilder;

    protected String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUserInternal(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.preference()))
                .findFirst()
                .orElseThrow(() -> new DataValidationException
                        ("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }

    protected T fromJsonToObject(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
