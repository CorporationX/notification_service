package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<Class<?>>> messageBuilders;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public String getMessage(Class<?> event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new DataValidationException
                        ("No message builder found for the given event type: " + event.getName()));
    }

    public void sendNotification(Long id, String message) {
        UserDto user = userServiceClient.getUserInternal(id);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.preference()))
                .findFirst()
                .orElseThrow(() -> new DataValidationException
                        ("No notification service found for the user's preferred communication method."))
                .send(user, message);
    }

    public void sendNotificationEvent(UserDto userDto, String message) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.preference()))
                .findFirst()
                .orElseThrow(() -> new DataValidationException
                        ("No notification service found for the user's preferred communication method."))
                .send(userDto, message);
    }

    public void preSendNotificationEvent(EventDto eventDto, UserDto userDto, String message){
        executorService.shutdown();
        long eventStart = eventDto.getStartDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        long start = Math.max(0, eventStart - now);

        executorService.schedule(()->sendNotificationEvent(userDto, message), start, TimeUnit.MILLISECONDS);
    }

    public T convertToJSON(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
