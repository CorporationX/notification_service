package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.FetchUserException;
import faang.school.notificationservice.exception.MappingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.ServiceNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> processingEvent) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            processingEvent.accept(event);
        } catch (Exception e) {
            throw new MappingException(eventType.getName(), message, e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() ->
                        new MessageBuilderNotFoundException(String.format(
                                "No message builder found for the given event type: %s", event.getClass().getName())));
    }

    protected void sendNotification(long receiverId, String message) {
        UserDto user;
        try {
            user = userServiceClient.getUser(receiverId);
        } catch (Exception e) {
            throw new FetchUserException(
                    String.format("Failed to fetch user with id: %d from user service: %s", receiverId, e.getMessage()),
                    e);
        }
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() ->
                        new ServiceNotFoundException(String.format(
                                "No notification service found for the user`s id: %d preferred method: %s",
                                receiverId, user.getPreference())))
                .send(user, message);
        log.info("Notification service sent notification \"{}\" to user with id {}.", message, receiverId);
    }
}
