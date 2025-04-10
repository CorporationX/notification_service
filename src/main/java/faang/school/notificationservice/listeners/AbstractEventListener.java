package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MappingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.ServiceNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
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
        } catch (JsonParseException e) {
            getCatchMappingExceptionData("Invalid JSON syntax", eventType, message, e);
        } catch (JsonMappingException e) {
            getCatchMappingExceptionData("JSON mapping failed", eventType, message, e);
        } catch (IOException e) {
            getCatchMappingExceptionData("Unexpected error during parsing", eventType, message, e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> {
                    String exceptionMessage = String.format(
                            "No message builder found for the given event type: %s", event.getClass().getName());
                    MessageBuilderNotFoundException e = new MessageBuilderNotFoundException(exceptionMessage);
                    log.error(exceptionMessage, e);
                    return e;
                });
    }

    protected void sendNotification(long receiverId, String message) {
        UserDto user = userServiceClient.getUser(receiverId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> {
                    String exceptionMessage = String.format(
                            "No notification service found for the user`s id: %d preferred method: %s",
                            receiverId, user.getPreference());
                    ServiceNotFoundException e = new ServiceNotFoundException(exceptionMessage);
                    log.error(exceptionMessage, e);
                    return e;
                })
                .send(user, message);
        log.info("Notification service sent notification \"{}\" to user with id {}.", message, receiverId);
    }

    private void getCatchMappingExceptionData(String cause, Class<T> eventType, Message message, Exception e) {
        log.error(String.format(cause + " Unable to parse event: %s with message: %s.",
                eventType.getName(), message), e);
        throw new MappingException(eventType.getName(), message, e);
    }
}
