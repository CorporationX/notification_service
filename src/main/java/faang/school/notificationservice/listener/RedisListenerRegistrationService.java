package faang.school.notificationservice.listener;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class RedisListenerRegistrationService {
    public void registerListener(RedisMessageListenerContainer container, Object listener, String channelName) {
        ChannelTopic topic = new ChannelTopic(channelName);
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(listener);
        container.addMessageListener(listenerAdapter, topic);
    }
}
