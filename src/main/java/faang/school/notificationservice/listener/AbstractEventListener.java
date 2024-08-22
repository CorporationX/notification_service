package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
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
    private final List<NotificationService> notificationServiceList;
    private final List<MessageBuilder<T>> messageBuilderList;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected String getMessage(T event, Locale userLocale, Object[] args) {
        return messageBuilderList.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageBuilder with " + event.getClass() + " type not found"))
                .buildMessage(event, userLocale, args);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServiceList.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NotificationService with preferred user contact " + user.getPreference() + " not found"))
                .send(user, message);
    }

    protected void sendNotificationToUsers(List<Long> userIds, String message) {
        for (Long userId : userIds) {
            sendNotification(userId, message);
        }
    }
}
