package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EventListenerException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(@NotNull Message message,@NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            log.debug("Event received: {}", event);
            consumer.accept(event);
        } catch (IOException e) {
            throw new EventListenerException(e);
        }
    }

    protected String getMessage(@NotNull T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event,
                        locale == null ? Locale.getDefault() : locale))
                .orElseThrow(() -> new EventListenerException("No suitable builder found for " + event.getClass()));
    }

    protected void sendNotification(@NotNull Long userId, @NotBlank String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        if (userDto == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new EventListenerException("Notification service for user preferred contact "
                        + userDto.getPreference() + " not found"))
                .send(userDto, message);
    }
}
