package faang.school.notificationservice.message.producer.impl;

import faang.school.notificationservice.config.redis.RedisConfig;
import faang.school.notificationservice.message.producer.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component(RedisConfig.REDIS_PUBLISHER_NAME)
@RequiredArgsConstructor
public class RedisMessagePublisher implements MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
