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

@RequiredArgsConstructor
@Component
public abstract class AbstractEventListener<T> {
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilder;
    protected final List<NotificationService> notificationService;
    protected final JsonMapper jsonMapper;

    public void sendNotification(String message, long userId) {
        UserDto userDto = userServiceClient.getUserDtoForNotification(userId);
        notificationService.stream()
                .filter(service -> service
                        .getPreferredContact() == userDto.getPreference())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(userDto.getPreference() + "is incorrect"))
                .send(userDto, message);
    }

    public String getMessage(T event, Locale locale) {
        return messageBuilder.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Incorrect message builder with " + event.getClass().getName()))
                .buildMessage(event, locale);
    }
}
