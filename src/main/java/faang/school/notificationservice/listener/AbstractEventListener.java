package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void sendMessage(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notification -> notification.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException
                        ("This service for sending notifications is not in the system")).
                send(user, message);
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream().
                filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException
                        ("This server does not process this type of event"))
                .buildMessage(event, locale);
    }

}
