package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DeserializeJsonException;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(Class<?> clazz, T event) {
        return messageBuilders.stream()
                .filter(builder -> builder.supports(clazz))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No message builder found"))
                .buildMessage(event, Locale.ENGLISH);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreferredContact())
                .forEach(service -> service.sendNotification(message));
    }

    protected T deserializeJson(Message message, Class<T> clazz) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new DeserializeJsonException("Failed to deserialize skill offer event");
        }
    }
}
