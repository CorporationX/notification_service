package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), getEventClass());
            handleEvent(event);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize event of type: " + getEventClass().getSimpleName(), e);
        }
    }

    protected abstract Class<T> getEventClass();

    protected abstract void handleEvent(T event);

    protected void sendNotification(UserDto user, String messageText) {
        NotificationService notificationService = notificationServices.get(user.getPreference());

        if (notificationService != null) {
            notificationService.send(user, messageText);
        } else {
            log.error("No matching notification service found for user preference: {}", user.getPreference());
        }
    }
}

