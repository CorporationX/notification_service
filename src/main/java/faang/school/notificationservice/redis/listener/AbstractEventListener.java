package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private final UserServiceClientMock userServiceClient;
    private final List<MessageBuilder<?>> messageBuilders;
    private final List<NotificationService> notificationServices;
    private final ObjectMapper objectMapper;
    private final Class<T> eventClass;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = message.toString();
            T event = objectMapper.readValue(jsonMessage, eventClass);
            processEvent(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert json to event");
        } catch (Exception exception) {
            log.error("Failed to process event from Redis", exception);
        }
    }

    protected abstract void processEvent(T event);

    protected UserDto getUserDto(Long userId) {
        return userServiceClient.getUser(userId);
    }

    protected MessageBuilder<T> defineBuilder() {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventClass))
                .map(builder -> (MessageBuilder<T>) builder)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No suitable MessageBuilder found for " + eventClass.getName()));
    }

    protected MessageBuilder<?> defineBuilder(Class<?> eventDtoClass) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventDtoClass))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "MessageBuilder not found for class: " + eventDtoClass.getName()));
    }

    protected NotificationService defineNotificationService(UserDto userDto) {
        return notificationServices.stream()
                .filter(ns -> ns.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "NotificationServices not found for preference: " + userDto.getPreference()));
    }

    protected String getMessage(T event, Locale locale) {
        MessageBuilder<T> messageBuilder = defineBuilder();
        return messageBuilder.buildMessage(event, locale);
    }

    protected void sendNotification(UserDto userDto, String message) {
        NotificationService notificationService = defineNotificationService(userDto);
        notificationService.send(userDto, message);
    }

    protected void sendNotification(Long userId, String message) {
        UserDto userDto = getUserDto(userId);
        sendNotification(userDto, message);
    }
}
