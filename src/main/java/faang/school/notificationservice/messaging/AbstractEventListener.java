package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == ProfileViewEvent.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException())
                .buildMessage(event, Locale.ENGLISH);
    }

    protected void sendNotification(long userId, String text) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new RuntimeException())
                .send(user, text);
    }
}
