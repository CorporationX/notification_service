package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;
    private final ObjectMapper mapper;

    public String getMessage(Class<?> clazz, T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getClass().equals(clazz))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Message builder not found"))
                .getMessage(locale, event);
    }

    public void sendNotification(UserDto user, String message) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .ifPresent(service -> service.send(user, message));
    }

    public T getEvent(Message message, Class<T> clazz) {
        try {
            return mapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Can't deserialize JSON");
        }
    }
}