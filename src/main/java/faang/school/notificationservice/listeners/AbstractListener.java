package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServicesList;
    protected final List<MessageBuilder<Class<?>>> messageBuildersList;


    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error deserializing JSON to object of type {} ", type.getName(), e);
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(Class<?> event, Locale locale) {
        return messageBuildersList.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No message constructor was found for the event: "
                        + event.getClass().getName()));
    }

    protected void sendNotification(Long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServicesList.stream()
                .filter(notificationServices -> notificationServices.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No preferred user contact was found"))
                .send(userDto, message);
        log.info("The message has been sent at {}", LocalDateTime.now().withNano(0));
    }
}
