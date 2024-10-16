package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<?>> messageBuilders;

    protected void handleEvent(Message message, Class<T> eventClass, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventClass);
            log.info("Event processed successfully: eventType={}", eventClass.getSimpleName());
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Failed to process event: eventType={}, messageBody={}",
                    eventClass.getSimpleName(), new String(message.getBody()), e);
            throw new EventProcessingException("Failed to process event of type " + eventClass.getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    protected String buildMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getSupportedClass().isAssignableFrom(event.getClass()))
                .findFirst()
                .map(builder -> {
                    log.info("Successfully built message for event type: {}", event.getClass().getSimpleName());
                    return ((MessageBuilder<T>) builder).buildMessage(event, userLocale);
                })
                .orElseThrow(() -> {
                    String errorMessage = "No message builder found for event type: " + event.getClass().getSimpleName();
                    log.error(errorMessage);
                    return new MessageBuilderNotFoundException(errorMessage);
                });
    }

    protected void sendNotification(UserDto userDto, String message) {
        log.debug("Attempting to send notification to user: {} with preferred notification means: {}",
                userDto.getId(), userDto.getPreference());

        NotificationService notificationService = notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No suitable notificationService found " +
                        "for user with id = %d, preference = %s", userDto.getId(), userDto.getPreference())));

        notificationService.send(userDto, message);
        log.info("Notification sent to user: {}, id = {} via {}", userDto.getUsername(), userDto.getId(),
                userDto.getPreference());
    }
}
