package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.exceptions.PreferredContactNotExistException;
import faang.school.notificationservice.message.event.NotificationEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;


@Slf4j
public abstract class AbstractEventListener<T extends NotificationEvent> {

    private final ObjectMapper mapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    protected AbstractEventListener(
            ObjectMapper mapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            MessageBuilder<T> messageBuilder) {

        this.mapper = mapper;
        this.userServiceClient = userServiceClient;
        this.notificationServices = notificationServices;
        this.messageBuilder = messageBuilder;
    }

    public void handleEvent(Message redisMessage, Class<T> eventType) {
        try {
            JsonNode jsonNode = mapper.readTree(redisMessage.getBody());
            T event = mapper.convertValue(jsonNode, eventType);
            UserForNotificationDto receiver = userServiceClient.getUserForNotificationById(event.getReceiverId());
            String message = messageBuilder.build(event, receiver.getLocaleFromLanguage());
            sendNotification(receiver, message);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Failed to convert message to {}", eventType.getSimpleName(), e);
        }
    }

    public void sendNotification(UserForNotificationDto receiver, String message) {
        notificationServices.stream()
                .filter(notificationService ->
                        receiver.isSamePreferredContact(notificationService.getPreferredContact()))
                .findFirst()
                .ifPresentOrElse(
                        notificationService -> notificationService.send(receiver, message),
                        () -> {
                            throw new PreferredContactNotExistException(receiver.preference().name());
                        });
    }
}
