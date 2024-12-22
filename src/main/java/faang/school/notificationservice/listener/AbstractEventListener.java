package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
            log.info("Event processed: {} ", event);
        } catch (IOException e) {
            log.error("Failed to handle event: {}", type, e);
            throw new RuntimeException(e);
        }
    }
}
