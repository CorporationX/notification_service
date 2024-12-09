package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationService;
    private final List<MessageBuilder<T>> messageBuilder;

    public void processEvent(Message message, Class<T> eventType, Consumer<T> processingEvent) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            processingEvent.accept(event);
        } catch (IOException e) {
            String exceptionMessage = String.format("Unable to parse event: %s, with message: %s",
                    eventType.getName(), message);
            MappingException mappingException = new MappingException(exceptionMessage, e);
            log.error(exceptionMessage, e);
            throw mappingException;
        }
    }

    public String getMessage(T event, Locale userLocale) {
        return messageBuilder.stream()
                .filter(messageBuilder -> messageBuilder.supportEventType() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> {
                    String exceptionMessage = String.format("Message wasn't found for event type - %s",
                            event.getClass().getName());
                    NotFoundException e = new NotFoundException(exceptionMessage);
                    log.error(exceptionMessage, e);
                    return e;
                });
    }

    public void sendNotification(long receiverId, String message) {
        UserDto user = userServiceClient.getUser(receiverId);
        notificationService.stream()
                .filter(notificationService -> notificationService.getPreferredContact().
                        equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    String exceptionMessage =
                            String.format("Notification service wasn't found for user notification preference - %s",
                                    user.getPreference());
                    NotFoundException e = new NotFoundException(exceptionMessage);
                    log.error(exceptionMessage, e);
                    return e;
                })
                .send(user, message);
        log.info(String.format("Notification service sent notification - %s. To user with id %d", message, receiverId));
    }
}
