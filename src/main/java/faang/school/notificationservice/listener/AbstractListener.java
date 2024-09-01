package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;
    private final Class<T> eventType;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e + "couldn't deserialize message");
        }
        List<UserDto> users = getUsersToNotify(event);
        users.forEach(user -> {
            long userId = user.getId();
            String msg = messageBuilder.buildMessage(event, Locale.US);
            sendNotification(userId, msg);
        });
    }

    public void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        if (user.getPreference() == null) {
            log.warn("User {} has no preference", userId);
            return;
        }
        notificationServices.stream().filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .forEach(service -> service.send(user, message));
    }

    protected abstract List<UserDto> getUsersToNotify(T event);
}
