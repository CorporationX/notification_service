package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final Map<EventType, MessageBuilder<T>> messageBuildersMap;
    protected final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;
    protected final UserContext userContext;


    public AbstractEventListener(List<MessageBuilder<T>> messageBuilders,
                                 ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 UserContext userContext) {
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
        this.userContext = userContext;

        this.messageBuildersMap = messageBuilders.stream()
                .collect(Collectors.toMap(
                        MessageBuilder::getEventType,
                        tMessageBuilder -> tMessageBuilder
                ));

        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                        NotificationService::getPreferredContact,
                        notificationService -> notificationService
                ));
    }

    public abstract EventType getEventType();

    public abstract String getTopicName();

    protected void handleEvent(Message message, Class<T> tClass, Consumer<T> consumer) {
        try {
            log.info("Received message: {}", new String(message.getBody()));
            T event = objectMapper.readValue(message.getBody(), tClass);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }

    protected String getMessage(T event) {
        log.info("Building message for event type: {} ", getEventType());
        MessageBuilder<T> messageBuilder = messageBuildersMap.get(getEventType());
        if (messageBuilder == null) {
            log.error("No message builder found for the given event type: {}", getEventType());
            throw new IllegalArgumentException("No message builder found for the given event type: "
                    + getEventType());
        }
        return messageBuilder.buildMessage(event, Locale.ENGLISH);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        log.info("User {} details retrieved", userId);

        NotificationService notificationService = notificationServiceMap.get(user.getPreference());
        if (notificationService == null) {
            log.error("No notification service found for the user's preferred communication method: {}",
                    user.getPreference());
            throw new IllegalArgumentException(
                    "No notification service found for the user's preferred communication method: "
                            + user.getPreference());
        }
        notificationService.send(user, message);
        log.info("Notification successfully sent to user ID: {}", user.getId());
    }
}
