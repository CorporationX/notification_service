package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.exception.kafka.InvalidKafkaMessageException;
import faang.school.notificationservice.exception.messaging.MessageBuilderNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractKafkaListener<T, R> {
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<R>> messageBuilders;
    private final ObjectMapper objectMapper;
    private final Class<T> eventClass;
    private final Class<R> messageModelClass;

    public T getEvent(String message) {
        try {
            return objectMapper.readValue(message, eventClass);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to deserialize Kafka message into class {}. Message: {}. Error: {}",
                    eventClass.getSimpleName(), message, ex.getMessage(), ex);
            throw new InvalidKafkaMessageException("Invalid message format", ex);
        }
    }

    public String getMessage(R event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == messageModelClass)
                .findAny()
                .orElseThrow(() -> {
                    log.warn("No MessageBuilder found for model class {}. Event: {}, Locale: {}",
                            messageModelClass.getSimpleName(), event, locale);
                    return new MessageBuilderNotFoundException("No MessageBuilder found for class " + messageModelClass.getSimpleName());
                })
                .buildMessage(event, locale);
    }

    public void sendNotification(UserClientResponseDto user, String text) {
        notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .findAny()
                .ifPresent(service -> service.send(user, text));
    }
}
