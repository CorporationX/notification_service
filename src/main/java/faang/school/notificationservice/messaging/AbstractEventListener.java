package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(Class<?> eventType, Locale userLocale, String... args) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(eventType))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(eventType, userLocale,args))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type: " + eventType.getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }
}
