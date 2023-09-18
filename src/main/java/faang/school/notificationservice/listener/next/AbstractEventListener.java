package faang.school.notificationservice.listener.next;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.next.MessageBuilder;
import faang.school.notificationservice.sender.NotificationServiceNext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T, V> {
    protected final ObjectMapper objectMapper;
    private final Map<UserDto.PreferredContact, NotificationServiceNext> notifications;
    private final Map<Class<?>, MessageBuilder> messageBuilders;
    protected final UserServiceClient userServiceClient;

    public String getMessage(Class<?> build, Locale locale, T event, V argument) {
        return messageBuilders.get(build).buildMessage(event, locale, argument);
    }

    public void sendNotification(long id, String topic, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        if (userDto.getPreference() == null) {
            return;
        }
        notifications.get(userDto.getPreference()).send(userDto, topic, message);
    }

    public String getMessageBody(Message message) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        log.info("Received message from channel '{}': {}", channel, messageBody);
        return messageBody;
    }

    public T getEvent(String messageBody, Class<T> type) {
        try {
            return (T) objectMapper.readValue(messageBody, type);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for {}", type.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
