package faang.school.notificationservice.listener;

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
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final MessageBuilder<T> messageBuilder;
    protected final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        NotificationService notificationService = notificationServices.get(user.getPreference());
        if (notificationService == null) {
            throw new IllegalArgumentException("No notification service found " +
                    "for the user's preferred communication method.");
        }
        notificationService.send(user, message);
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
