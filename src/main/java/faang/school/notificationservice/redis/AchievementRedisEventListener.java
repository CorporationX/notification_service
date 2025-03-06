package faang.school.notificationservice.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.mapper.EventMapper;
import faang.school.notificationservice.redis.event.AchievementRedisEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementRedisEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final EventMapper eventMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received message: {}", message);
        try {
            AchievementRedisEvent event = objectMapper.readValue(message.getBody(),
                    AchievementRedisEvent.class);
            eventPublisher.publishEvent(eventMapper.toEvent(event, this));
        } catch (IOException e) {
            log.error("Error while processing message", e);
        }
    }
}
