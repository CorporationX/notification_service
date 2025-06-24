package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T extends NotificationEvent> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;
    private final List<MessageBuilder<T>> messageBuilders;

    protected abstract boolean isEventValid(T event);

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected void sendNotification(T event) {
        if (isEventValid(event)) {
            sendMessage(event.getOwner(), getMessage(event));
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    protected void sendNotification(Long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        sendMessage(userDto, message);
    }

    protected String getMessage(T event) {
        Locale locale = Objects.isNull(event.getOwner().getLocale())
                ? LocaleContextHolder.getLocale()
                : event.getOwner().getLocale();
        return messageBuilder.buildMessage(event, locale);
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> Objects.equals(messageBuilder.getInstance(), event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder was found for the given type: "
                        + event.getClass().getName()));
    }

    protected void sendMessage(UserDto userDto, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No notification service was found for user preferred notification type"))
                .send(userDto, message);
    }

    @SafeVarargs
    protected final boolean validateObjectNonNullData(Object o, Supplier<Object>... fieldGetters) {
        return Objects.nonNull(o) || Arrays.stream(fieldGetters).map(Supplier::get).noneMatch(Objects::isNull);
    }
}