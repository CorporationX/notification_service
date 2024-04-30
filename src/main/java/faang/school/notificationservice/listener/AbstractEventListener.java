package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final MessageBuilder<T> messageBuilder;
    protected final List<NotificationService> notificationServices;
    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;
    protected final Class<T> eventType;

    protected abstract void sendSpecifiedNotification(T event);

    protected String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .ifPresentOrElse(service -> service.send(user, message), () -> {
                    throw new IllegalStateException("No notification service found for " + user.getPreference());
                });
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            sendSpecifiedNotification(event);
        } catch (IOException e) {
            log.error("IOException was thrown", e);
            throw new SerializationException("Failed to deserialized a message", e);
        }
    }
}
