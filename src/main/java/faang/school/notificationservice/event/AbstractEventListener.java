package faang.school.notificationservice.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final MessageBuilder<T> messageBuilder;
    private final List<NotificationService> notificationServices;

    public void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilder.buildMessage(event, userLocale);
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification service "
                        + user.getPreference() + " not defined!"))
                .send(user, message);
    }
}
