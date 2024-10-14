package faang.school.notificationservice.config.redis.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {
    MessageListenerAdapter getAdapter();

    Topic getTopic();
}
