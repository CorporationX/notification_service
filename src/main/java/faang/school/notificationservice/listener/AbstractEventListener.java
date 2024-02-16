package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class AbstractEventListener<T> implements MessageListener {
    private ObjectMapper objectMapper;
    private Class<T> eventType;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public AbstractEventListener(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            processEvent(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void processEvent(T event);
}
