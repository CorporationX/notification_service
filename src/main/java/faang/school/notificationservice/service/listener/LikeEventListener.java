package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.exception.LikeEventProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
        } catch (IOException e) {
            throw new LikeEventProcessingException("Failed to process LikeEvent message");
        }
    }
}
