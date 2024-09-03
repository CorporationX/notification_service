package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            eventConsumer.accept(event);
        } catch (IOException e) {
            String errorMessage = "Failed to process event of type %s. Error details: %s"
                    .formatted(eventType, e.getMessage());
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for event type: %s"
                        .formatted(event.getClass().getName())));
    }

    protected void sendNotification(UserDto userDto, String message) {
        notificationServices.stream()
//                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .filter(service -> service.getPreferredContact() == UserDto.PreferredContact.EMAIL)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found for preference: %s"
                        .formatted(userDto.getPreference())))
                .send(userDto, message);
    }
}
