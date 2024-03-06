package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.messageBuilders.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public abstract class AbstractEventListener<T> implements MessageListener {
    protected ObjectMapper objectMapper;
    protected UserServiceClient userServiceClient;
    private List<NotificationService> notificationServices;
    private List<MessageBuilder<T>> messageBuilders;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setUserServiceClient(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Autowired
    public void setNotificationServices(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    @Autowired
    public void setMessageBuilders(List<MessageBuilder<T>> messageBuilders) {
        this.messageBuilders = messageBuilders;
    }

    protected String getMessage(T event) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message builder not found"))
                .buildMessage(event, Locale.UK);
    }

    @Retryable(retryFor = FeignException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    protected void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Preferred contact not found"))
                .send(user, message);
    }

    protected T getEvent(byte[] message, Class<T> eventTypeClass) {
        try {
            return objectMapper.readValue(message, eventTypeClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
