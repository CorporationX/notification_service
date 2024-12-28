package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener{
    private final ObjectMapper objectMapper;
    private final Class<T> eventClass;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received message on goal channel: {}", message);
        try {
            T event = objectMapper.readValue(message.getBody(), eventClass);
            handleEvent(event);
        } catch (IOException e) {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("Error while deserializing {} from Redis. Error: {}", messageBody, e.getMessage(), e);
        }
    }

    protected abstract void handleEvent(T event);
}
