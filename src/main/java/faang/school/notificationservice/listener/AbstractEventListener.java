package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected String buildMessage(T event, Locale locale) {
       return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("no message builder found for " + event.getClass().getName()));
    }

    protected void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(notificationService -> userDto.getPreference().equals(notificationService.getPreferredContact()))
                .findFirst()
                .ifPresent(notificationService -> notificationService.send(userDto, message));
    }
}
