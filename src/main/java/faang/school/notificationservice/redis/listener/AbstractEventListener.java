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

    protected void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        NotificationService notificationService = defineNotificationService(userDto);
        notificationService.send(userDto, message);
    }
}
