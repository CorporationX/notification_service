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
    protected final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new DataValidationException
                        ("No message builder found for the given event type: " + event.getClass().getName()));
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

    public T convertToJSON(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
