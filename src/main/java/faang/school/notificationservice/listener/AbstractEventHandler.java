package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventHandler<T> implements MessageListener {
    protected final RedisMessageListenerContainer container;
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder> messageBuilders;
    protected final List<NotificationService> notificationServices;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(getTopicName()));
        log.info("Initializing listener {}, topic {}", this.getClass().getName(), getTopicName());
    }

    protected void handleEvent(Message message, Class<T> clazz, Consumer<T> consumer) {
        try {
            T event = (T) objectMapper.readValue(message.getBody(), clazz);
            log.info("PArced project follower event: {}", event);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Parsing error = {}, {}, for message = {}", e.getMessage(), e, message);
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst()
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> {
                    log.error("No message builder found for the event type:" + event.getClass().getName());
                    return new IllegalArgumentException("No message builder found for the event type:" + event.getClass().getName());
                });
    }

    protected void sendNotification(long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .ifPresent(notificationService -> notificationService.send(userDto, message));
    }

    protected abstract String getTopicName();
}
