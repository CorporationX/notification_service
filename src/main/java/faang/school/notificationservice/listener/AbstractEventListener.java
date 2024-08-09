package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;

    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("There is no message builder for the given event type" + event.getClass().getName()));
    }

    protected void sendNotification(long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("There's no notification services for user's preferred communications"))
                .send(user, message);
    }

}
