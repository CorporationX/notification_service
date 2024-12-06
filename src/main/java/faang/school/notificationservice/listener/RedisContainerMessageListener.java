package faang.school.notificationservice.listener;

import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {
    MessageListenerAdapter getAdapter();
    Topic getTopic();
}
