package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messageBulder.MessageBuilder;
import faang.school.notificationservice.model.PreferredContact;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationService;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type" + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        //UserDto user = userServiceClient.getUser(id);
        notificationService.stream()
                .filter(notificationServices -> notificationServices.getPreferredContact().equals(PreferredContact.EMAIL))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for the user's prefferend communication method."))
                .send(new UserDto(), message);
    }
    public T convertToJSON(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
