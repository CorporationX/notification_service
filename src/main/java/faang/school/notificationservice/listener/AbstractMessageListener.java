package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;

    protected void handleEvent(Class<T> klass, Message message, Consumer<T> callback) {
        try {
            T event = objectMapper.readValue(message.getBody(), klass);
            callback.accept(event);
        } catch (IOException e) {
            log.error("Failed to deserialize event of type {}", klass.getSimpleName(), e);
            throw new RuntimeException("Failed to deserialize event of type: " + klass.getSimpleName(), e);
        }
    }
}

