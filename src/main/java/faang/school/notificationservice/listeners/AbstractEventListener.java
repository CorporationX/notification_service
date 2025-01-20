package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<MessageBuilder<T>> messageBuilders;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No message builder found for given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notifications service found for user's preferred communication method."))
                .send(userDto, message);
    }

    protected T getEvent(Message message, Class<T> clazz) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
