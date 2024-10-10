package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
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

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No message builder for class"))
                .buildMessage(event, userLocale);
    }

    protected void sendNotification(UserDto user, String message) {
        log.info("sendNotification() - start, userId - {}", user.getId());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getNotifyPreference()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("The user has not specified a preferred notification method"))
                .send(user, message);
        log.info("sendNotification() - finished, userId - {}", user.getId());
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            log.info("handleEvent() - start");
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
            log.info("handleEvent() - finish, object - {} ", event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
