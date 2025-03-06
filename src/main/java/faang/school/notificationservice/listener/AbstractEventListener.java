package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    public abstract void onMessage(T event);

    protected void handleMessage(T event, long userId) {
        UserDto user = userServiceClient.getUser(userId);
        String message = getMessage(event, user.getLocale());
        sendNotification(user, message);
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new RuntimeException("Message builder not found"));
    }

    protected void sendNotification(UserDto userDto, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification service not found"))
                .send(userDto, message);
    }
}