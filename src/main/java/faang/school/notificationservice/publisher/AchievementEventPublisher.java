package faang.school.notificationservice.publisher;

import faang.school.notificationservice.dto.AchievementEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AchievementEventPublisher implements MessagePublisher<AchievementEvent> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public AchievementEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                     @Qualifier("achievementTopic") ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @PostMapping(value = "/achievement", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> publish(@RequestBody AchievementEvent achievementEvent) {
        redisTemplate.convertAndSend(topic.getTopic(), achievementEvent);
        return ResponseEntity.ok("{\"message\": \"Event published successfully\"}");
    }
}
