package faang.school.notificationservice.listeners.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(T eventType, Locale userLocale) {
        return messageBuilders.stream()
                .filter(mb -> mb.supportsEvent(eventType))
                .findFirst()
                .map(mb -> mb.buildMessage(eventType, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("No one message was found for the given event type " + eventType.getClass().getName()));
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No one notification was found for the given user " + userId + " as preferred contact"))
                .send(userDto, message);
    }

    private Class<?> getEventClass(T eventType) {
        return eventType.getClass();
    }

    public T constructEvent(byte[] message, Class<T> eventClass) {
        try {
            return objectMapper.readValue(message, eventClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessage(T event, long receiverId, Locale userLocale) {
        String msg = getMessage(event, userLocale);
        sendNotification(receiverId, msg);
    }
}
