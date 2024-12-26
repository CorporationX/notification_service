package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.EventHandler;
import faang.school.notificationservice.exception.InvalidMessageException;
import faang.school.notificationservice.exception.EventDeserializationException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final List<EventHandler<T>> eventHandlers;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    protected abstract void handleEvent(T event);

    @SuppressWarnings("unchecked")
    protected Class<T> getEventType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    protected T listenEvent(Message message) {
        if (message.getBody().length == 0) {
            log.error("Message body is empty: {}", message.getBody());
            throw new InvalidMessageException("Message body is empty");
        }

        try {
            return objectMapper.readValue(message.getBody(), getEventType());
        } catch (IOException e) {
            log.error("Error deserializing message: {}", new String(message.getBody()), e);
            throw new EventDeserializationException("Failed to map message to event type");
        }
    }

    protected void sendNotification(UserContactsDto receiverDto, T event, String message) {
        createMessage(event);

        if (receiverDto.getPreference() == null) {
            log.error("User {} has no preference set", receiverDto.getId());
            return;
        }

        notificationServices.stream()
                .filter(service -> receiverDto.getPreference().equals(service.getPreferredContact()))
                .findFirst()
                .ifPresentOrElse(
                        service -> service.send(receiverDto, message),
                        () -> log.error("No notification service found for user {}", receiverDto.getId())
                );

        log.info("Message sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());
    }

    protected String createMessage(T event) {
        return messageBuilder.buildMessage(event, LocaleContextHolder.getLocale());
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
            T event = listenEvent(message);

            if (eventHandlers != null && !eventHandlers.isEmpty()) {
                log.info("Processing event with handlers: {}", eventHandlers);
                eventHandlers.forEach(handler -> handler.handle(event));
            } else {
                log.warn("No event handlers available for event: {}", event);
            }

            log.info("Data successfully processed for event {}", event);
            handleEvent(event);
    }
}
