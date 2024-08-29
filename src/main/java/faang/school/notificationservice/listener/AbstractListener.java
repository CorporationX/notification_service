package faang.school.notificationservice.listener;

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

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e + "couldn't deserialize message");
        }
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
}
