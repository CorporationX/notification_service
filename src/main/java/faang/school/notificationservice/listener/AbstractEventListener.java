package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ExceptionMessages;
import faang.school.notificationservice.exception.feign.UserServiceException;
import faang.school.notificationservice.exception.listener.EventHandlingException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Abstract base class for event listeners that handle events from Redis topics.
 *
 * @param <T> the type of event this listener handles
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    /**
     * Serves as an entry point to handle events from Redis topics.
     * Start an implementation of a listener by invoking this method with the specified parameters,
     * then call the {@link #getMessage(Object, Locale)} inside the consumer to prepare the message to be sent.
     * Final step is to invoke the {@link #sendNotification(Long, String)} method to send the message to the user.
     *
     * @param message       the Redis message containing the event data
     * @param type          the class type of the event
     * @param eventConsumer the consumer to process the event
     * @throws IllegalArgumentException if the event cannot be handled
     */
    protected void handleEvent(Message message, Class<T> type, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Received event: {}", event);
            eventConsumer.accept(event);
        } catch (IOException e) {
            log.error(ExceptionMessages.EVENT_HANDLING_FAILURE, e);
            throw new EventHandlingException(e);
        }
    }

    /**
     * Builds a message for the given event and user locale using the appropriate message builder based on the event's type.
     *
     * @param event      the event for which to build the message
     * @param userLocale the locale of the user
     * @return the built message
     * @throws IllegalArgumentException if no message builder is found for the event
     */
    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(ExceptionMessages.NO_MESSAGE_BUILDER_FOUND, event)))
                .buildMessage(event, userLocale);
    }

    /**
     * Sends a notification to the user with the given ID and message. The notification will be sent via the user's preferred contact method.
     *
     * @param id      the ID of the user
     * @param message the message to be sent
     * @throws IllegalArgumentException if no notification service is found for the user
     */
    protected void sendNotification(Long id, String message) {
        var user = fetchUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(ExceptionMessages.NO_NOTIFICATION_SERVICE_FOUND, user)))
                .send(user, message);
    }

    private UserDto fetchUser(Long id) {
        UserDto user;
        try {
            user = userServiceClient.getUser(id);
        } catch (FeignException | RestClientException exception) {
            throw new UserServiceException(String.format(ExceptionMessages.USER_DATA_FETCH_FAILURE, id), exception);
        }
        return user;
    }
}
