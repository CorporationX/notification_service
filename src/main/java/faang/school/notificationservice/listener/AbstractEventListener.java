package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractEventListener<T extends NotificationEvent> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userClient;
    private final Map<Class<?>, MessageBuilder<T>> messageBuilders;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    public AbstractEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userClient,
            List<MessageBuilder<T>> messageBuildersList,
            List<NotificationService> notificationServicesList
    ) {
        this.objectMapper = objectMapper;
        this.userClient = userClient;
        this.messageBuilders = messageBuildersList.stream()
                .collect(Collectors.toMap(MessageBuilder::getInstance, b -> b));
        this.notificationServices = notificationServicesList.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, s -> s));
    }

    protected void handleEvent(Message message, Class<T> eventType) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            long receiverId = event.getReceiverId();
            UserDto receiver = userClient.getUser(receiverId);
            sendNotification(receiver, getMessage(event, Locale.ENGLISH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        MessageBuilder<T> messageBuilder = messageBuilders.get(event.getClass());

        if (messageBuilder == null) {
            throw new IllegalArgumentException(
                    "not found any message builder for event type: " + event.getClass().getName()
            );
        }

        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.get(user.getPreference());

        if (notificationService == null) {
            throw new IllegalArgumentException(
                    "not found any notification service for user's preferred contact: " + user.getPreference()
            );
        }

        notificationService.send(user, message);
    }
}
