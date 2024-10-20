package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<T> messageBuilder;
    private final List<NotificationService> notificationServices;

    public T handleEvent(Message message, Class<T> type) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(message.getBody(), type);
        } catch (IOException ex) {
            throw new DataValidationException("Error during reading message from redis topic", ex);
        }
    }

    public String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        System.out.println("message is " + message);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification type was not found in existing options"))
                .send(user, message);
    }
}