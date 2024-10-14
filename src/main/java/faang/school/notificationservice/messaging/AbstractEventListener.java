package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> eventClass, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventClass);
            log.info("Received {}: {}", eventClass, event);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error while handling {} ({}) from Redis message", eventClass, new String(message.getBody()), e);
        }
    }

    protected String buildText(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getSupportedClass().equals(event.getClass()))
                .findFirst()
                .map(builder -> builder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No suitable messageBuilder found for event " + event.getClass().getSimpleName()));
    }

    protected void sendNotification(Long userId, String text) {
        UserDto userDto = userServiceClient.getUser(userId);
        UserDto.PreferredContact preference = userDto.getPreference();
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == preference)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No suitable notificationService found for user with id" + userDto.getId() + ", preference " + preference))
                .send(userDto, text);
    }
}
