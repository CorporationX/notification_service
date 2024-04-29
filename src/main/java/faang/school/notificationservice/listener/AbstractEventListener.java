package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final Class<T> type;
    protected final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event = convert(message.getBody());

        workingEvent(event);
        log.info("event processed successfully {}", event);
    }

    private T convert(byte[] body) {
        try {
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void workingEvent(T event);

}
