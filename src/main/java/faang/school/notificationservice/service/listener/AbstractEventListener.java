package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final Map<Class<?>, MessageBuilder<?>> messageBuilders;
    protected final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) Optional.ofNullable(messageBuilders.get(event.getClass()))
                .orElseThrow(() -> new NoSuchElementException("No message builder found for given event type"));

        return messageBuilder.buildMessage(event, userLocale);
    }

    protected void sendNotification(UserDto user, String message) {
        NotificationService notificationService = Optional.ofNullable(notificationServices.get(user.getPreference()))
                .orElseThrow(() -> new NoSuchElementException("Not found notification service"));

        notificationService.send(user, message);
    }
}
