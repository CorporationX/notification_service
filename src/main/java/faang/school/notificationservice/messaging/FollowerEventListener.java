package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
