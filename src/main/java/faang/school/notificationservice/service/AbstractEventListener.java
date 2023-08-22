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
public abstract class AbstractEventListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder> messageBuilders;

    public String getMessage(Class<?> eventType, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(eventType))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(eventType, locale))
                .orElseThrow(() -> new DataValidationException
                        ("No message builder found for the given event type: " + eventType.getName()));
    }

    public void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new DataValidationException
                        ("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}
