package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MessageDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.PostService;
import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;


@RequiredArgsConstructor
public abstract class RedisAbstractMessageSubscriber<T> {
    protected final ObjectMapper objectMapper;
    protected final UserService userService;
    protected final PostService postService;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, MessageDto messageDto,
                                Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilders -> messageBuilders.supportsEventType() == event.getClass())
                .findFirst()
                .map(messageBuilders -> messageBuilders.buildMessage(event, messageDto, userLocale))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No message builder found the given event type: " + event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userService.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service found for the user's preferred communication method."))
                .send(user, message);
    }

}
