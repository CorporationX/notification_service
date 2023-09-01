package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.notification.MessageBuilder;
import faang.school.notificationservice.notification.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractionEventListener<T> {

    protected final JsonMapper jsonMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportEventType().equals(event.getClass()))
                .findFirst()
                .map(messageBuild -> messageBuild.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("Message builder not found fo the given event type" + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification service for User's preferred communication not found"))
                .send(userDto, message);
    }
}
