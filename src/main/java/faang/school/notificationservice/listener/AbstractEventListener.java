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
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final Map<Class<?>, MessageBuilder<?>> messageBuilders;
    protected final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocale) {
        log.info("getMessage() - start");

        @SuppressWarnings("unchecked")
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) Optional.ofNullable(messageBuilders.get(event.getClass()))
                .orElseThrow(() -> {
                    log.error("Not found message builder for - {}", event.getClass());
                    return new NoSuchElementException("Not found message builder");
                });

        String message = messageBuilder.buildMessage(event, userLocale);
        log.info("getMessage() - finish, message - {}", message);
        return message;
    }

    protected void sendNotification(UserDto user, String message) {
        log.info("sendNotification() - start, userId - {}", user.getId());
        UserDto.PreferredContact userPreferredNotification = user.getNotifyPreference();

        NotificationService notificationService = Optional.ofNullable(notificationServices.get(userPreferredNotification))
                .orElseThrow(() -> {
                    log.error("Not found notification service for preferred by user - {}", userPreferredNotification);
                    return new NoSuchElementException("Not found notification service");
                });
        notificationService.send(user, message);
        log.info("sendNotification() - finished, userId - {}", user.getId());
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            log.info("handleEvent() - start");
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("handleEvent() - event - {}", event);
            consumer.accept(event);
            log.info("handleEvent() - finish, object - {} ", event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
