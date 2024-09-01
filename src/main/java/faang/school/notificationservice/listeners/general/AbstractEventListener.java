package faang.school.notificationservice.listeners.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.Notifiable;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T extends Notifiable> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    @Override
    public void onMessage(@Nonnull Message message, byte[] pattern) {
        log.info("{} received message from Redis: {}", this.getClass().getName(), message);
        log.debug("Channel: {}", new String(pattern, StandardCharsets.UTF_8));

        T typedEvent = constructEvent(message.getBody(), getEventClassType());
        sendMessage(typedEvent, typedEvent.getReceiverId(), Locale.US);
    }

    protected abstract Class<T> getEventClassType();

    private String getMessage(T typedEvent, Locale userLocale) {
        return messageBuilders.stream()
                .filter(mb -> mb.supportsEvent().equals(typedEvent.getClass()))
                .findFirst()
                .map(mb -> mb.buildMessage(typedEvent, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No one message was found for the given event type " + typedEvent.getClass().getName()));
    }

    private void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No one notification was found for the given user " + userId + " as preferred contact"))
                .send(userDto, message);
    }

    private T constructEvent(byte[] messageBody, Class<T> eventClass) {
        try {
            return objectMapper.readValue(messageBody, eventClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMessage(T event, long receiverId, Locale userLocale) {
        String msg = getMessage(event, userLocale);
        sendNotification(receiverId, msg);
    }
}
