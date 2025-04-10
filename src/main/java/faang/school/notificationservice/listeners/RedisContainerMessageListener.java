package faang.school.notificationservice.listeners;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface RedisContainerMessageListener {

    default MessageListenerAdapter getAdapter() {
        return new MessageListenerAdapter(this);
    }

    ChannelTopic getTopic();
}
