package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.GoalCompletedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEventDto event = objectMapper.readValue(message.getBody(), GoalCompletedEventDto.class);
        } catch (Exception e) {
            log.error("Failed to deserialize message", e);
            throw new RuntimeException(e);
        }
    }
}
