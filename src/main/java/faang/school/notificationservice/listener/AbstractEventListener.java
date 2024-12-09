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
    protected final List<MessageBuilder> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for the event type:" + event.getClass().getName()));
    }

    protected void sendNotification(long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .ifPresent(notificationService -> notificationService.send(userDto, message));
    }
}
