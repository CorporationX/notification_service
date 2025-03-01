package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.service.NotificationStrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    protected final NotificationStrategyService notificationStrategyService;

    protected T getEventFromBytes(byte[] body, Class<T> eventType) {
        try {
            return objectMapper.readValue(body, eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
