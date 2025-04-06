package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidPreferredContactException;
import faang.school.notificationservice.exception.UnsupportedLocaleException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static faang.school.notificationservice.messages.ErrorMessages.ERROR_DESERIALIZING_MESSAGE;
import static faang.school.notificationservice.messages.ErrorMessages.NO_MESSAGE_BUILDER_FOUND_FOR_LOCALE;
import static faang.school.notificationservice.messages.ErrorMessages.NO_NOTIFICATION_SERVICE_FOUND_FOR_PREFERRED_COMMUNICATION;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEvenListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void processEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            log.error(ERROR_DESERIALIZING_MESSAGE, message.getBody(), e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> new UnsupportedLocaleException(
                        NO_MESSAGE_BUILDER_FOUND_FOR_LOCALE.formatted(locale)));
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new InvalidPreferredContactException(
                        NO_NOTIFICATION_SERVICE_FOUND_FOR_PREFERRED_COMMUNICATION.formatted(user.getPreference())))
                .send(user, message);
    }
}
