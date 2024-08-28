package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final List<EventHandler<T>> handlers;

    protected abstract T handleEvent(Message message) throws IOException;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = handleEvent(message);
        } catch (IOException e) {
            throw new RuntimeException(e + "couldn't deserialize message");
        }
        handlers.forEach(handler -> handler.handle(event));
        log.info("{} events have been handled", handlers.size());
    }
}
