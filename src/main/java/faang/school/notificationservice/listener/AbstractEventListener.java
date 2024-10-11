package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<?>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            log.info("Event processed successfully: eventType={}", eventType.getSimpleName());
            eventConsumer.accept(event);
        } catch (IOException exception) {
            log.error("Failed to process event: eventType={}, messageBody={}",
                    eventType.getSimpleName(), new String(message.getBody()), exception);
            throw new EventProcessingException("Failed to process event of type " + eventType.getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    protected String getMessage(T event, Locale locale) {
        for (MessageBuilder<?> messageBuilder : messageBuilders) {
            if (messageBuilder.getInstance().isAssignableFrom(event.getClass())) {
                log.info("Successfully built message for event type: {}", event.getClass().getSimpleName());
                return ((MessageBuilder<T>) messageBuilder).buildMessage(event, locale);
            }
        }

        String errorMessage = "No message builder found for event type: " + event.getClass().getSimpleName();
        log.error(errorMessage);
        throw new MessageBuilderNotFoundException(errorMessage);
    }

    protected void sendNotification(UserDto userDto, String message) {
        log.debug("Attempting to send notification to user: {} with preferred notification means: {}",
                userDto.getId(), userDto.getPreference());

        NotificationService notificationService = notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for preferred " +
                        "notification means: " + userDto.getPreference()));

        notificationService.send(userDto, message);
        log.info("Notification sent to user: {} via {}", userDto.getUsername(), userDto.getPreference());
    }
}
