package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServiceList;
    protected final List<MessageBuilder<T>> messageBuilderList;
    protected final ObjectMapper objectMapper;

    protected String getMessage(T event, Locale locale) {
        return messageBuilderList.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("There are no message builder for event: "+event.getClass().getName()));
    }

    protected void sendNotification(Long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServiceList.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There are no preffered contact"))
                .send(userDto, message);
        log.info("The message has been sent");
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON to object of type " + type.getName());
        }
    }
}
