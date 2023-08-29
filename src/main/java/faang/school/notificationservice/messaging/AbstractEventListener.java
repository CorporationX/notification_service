package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AbstractEventListener<T> {
    protected final JsonMapper jsonMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No message builder for " + event.getClass().getName()))
                .buildMessage(event, locale);
    }

    protected void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(userDto.getPreference() + " isn`t supported"))
                .send(userDto, message);
    }
}
