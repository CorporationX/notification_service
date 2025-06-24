package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationList;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(String message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message, type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder was found for the given type: "
                        + event.getClass().getName()));
    }

    protected void sendNotification(UserDto userDto, String message) {
        notificationList.stream()
                .filter(notificationService -> userDto.getPreference() == notificationService.getPreferredContact())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service was found for user preferred notification type"))
                .send(userDto, message);
    }
}
