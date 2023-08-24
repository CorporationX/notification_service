package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.massages.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new DataValidationException
                        ("No message builder found for the given event type: " + event.getClass().getName()));
    }

    public void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUserInternal(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.preference()))
                .findFirst()
                .orElseThrow(() -> new DataValidationException
                        ("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}
