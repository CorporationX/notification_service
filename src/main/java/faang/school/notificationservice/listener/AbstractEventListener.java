package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final UserServiceClient userServiceClient;
    protected final ObjectMapper objectMapper;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;
    private final Map<Class<?>, MessageBuilder<T>> messageBuilders;

    @Autowired
    public AbstractEventListener(List<NotificationService> notificationServices,
                                 List<MessageBuilder<T>> messageBuilders,
                                 UserServiceClient userServiceClient,
                                 ObjectMapper objectMapper) {
        this.notificationServices = notificationServices
                .stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact,
                        notificationService -> notificationService));
        this.messageBuilders = messageBuilders
                .stream()
                .collect(Collectors.toMap(MessageBuilder::getInstance, messageBuilder -> messageBuilder));
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

    protected T handleEvent(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        MessageBuilder<T> messageBuilder = messageBuilders.get(event.getClass());
        if (messageBuilder == null) {
            log.error("No message builder found for event {}", event.getClass().getName());
            throw new IllegalArgumentException("No such message builder found " + event.getClass().getName());
        }
        return messageBuilder.buildMessage(event, locale);
    }

    @Async("taskExecutor")
    protected void sendNotification(Long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        NotificationService notificationService = notificationServices.get(userDto.getPreference());
        if (notificationService == null) {
            log.error("Not found notification service for user {} with preferred contact {}", id, userDto.getPreference());
            throw new IllegalArgumentException("No such notification service found " + userDto.getPreference());
        }
        notificationService.send(userDto, message);
    }
}
