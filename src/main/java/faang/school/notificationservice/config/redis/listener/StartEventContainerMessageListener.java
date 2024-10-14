package faang.school.notificationservice.config.redis.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class StartEventContainerMessageListener implements RedisContainerMessageListener {
    private final MessageListenerAdapter adapter;
    private final Topic topic;

    public StartEventContainerMessageListener(MessageListener eventStartEventListener, Topic eventStartTopic) {
        this.adapter = new MessageListenerAdapter(eventStartEventListener);
        this.topic = eventStartTopic;
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
