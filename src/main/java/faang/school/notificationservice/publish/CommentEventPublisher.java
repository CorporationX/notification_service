package faang.school.notificationservice.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventPublisher implements EventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic commentTopicEvent;

    public void publish(String event) {
        redisTemplate.convertAndSend(commentTopicEvent.getTopic(), event);
    }
}
