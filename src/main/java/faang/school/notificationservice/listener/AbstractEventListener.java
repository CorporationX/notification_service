package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> services;


    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        T event = null;
        try {
            event = objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        consumer.accept(event);
    }

    protected void sendMessage(T eventDto, long userId) {
        UserDto user = userServiceClient.getUser(userId);

        String message = messageBuilders.stream()
                .filter(messageBuilders -> messageBuilders.getEventType() == eventDto.getClass())
                .findFirst()
                .map(messageBuilders -> messageBuilders.buildMessage(user, eventDto))
                .orElseThrow(() -> new IllegalArgumentException
                        ("No message builder found for the given event type: " + eventDto.getClass().getName()));
        services.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(user, message));
    }
}
