package faang.school.notificationservice.config.redis;

import lombok.Getter;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Getter
public class ChannelListenerAdapter {
    private final MessageListenerAdapter listenerAdapter;
    private final ChannelTopic topic;

    public ChannelListenerAdapter(MessageListener listener, String topic) {
        this.listenerAdapter = new MessageListenerAdapter(listener);
        this.topic = new ChannelTopic(topic);
    }
}
