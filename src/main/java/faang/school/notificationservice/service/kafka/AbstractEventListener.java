package faang.school.notificationservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error_message.ErrorMessage;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.messageBuilder.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServiceList;

    protected void handleEvent(ConsumerRecord<String, String> record, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(record.value(), type);
            consumer.accept(event);
        } catch (JsonProcessingException e) {
            log.error(ErrorMessage.ERROR_NOTIFICATION, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            log.error(ErrorMessage.ERROR_NOTIFICATION, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilders -> messageBuilders.supportsEventType().equals(event.getClass()))
                .findFirst()
                .map(messageBuilders -> messageBuilders.buildMessage(event, userLocale))
                .orElseThrow(() -> {
                    log.error(ErrorMessage.getErrorNotFoundMessageBuilder(event.getClass().getName()));
                    return new IllegalArgumentException(ErrorMessage.getErrorNotFoundMessageBuilder(event.getClass().getName()));
                });
    }

    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        user.setPreference(userServiceClient.getPreferredContact(id));
        notificationServiceList.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error(ErrorMessage.ERROR_NOT_FOUND_NOTIFICATION);
                    return new IllegalArgumentException(ErrorMessage.ERROR_NOT_FOUND_NOTIFICATION);
                })
                .send(user, message);
    }
}
