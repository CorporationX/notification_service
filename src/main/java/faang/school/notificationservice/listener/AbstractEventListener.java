package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationStrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    protected final NotificationStrategyService notificationStrategyService;
    protected final UserServiceClient userServiceClient;
    protected final MessageBuilder<T> messageBuilder;

    public T handleEvent(byte[] body, Class<T> eventType) {
        try {
            return objectMapper.readValue(body, eventType);
        } catch (IOException e) {
            log.error("Ошибка при чтении События из Redis: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendNotification(UserDto userDto, String message) {
        notificationStrategyService.getNotificationService(userDto)
                .send(userDto, message);
    }
}
