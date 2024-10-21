package faang.school.notificationservice.config.redis.channel.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

//@Component
public class GoalChannelListenerAdapter extends AbstractChannelListenerAdapter {
    public GoalChannelListenerAdapter(
            @Value("${spring.data.redis.channels.goal-channel.name}") String topic,
            @Qualifier("goalCompletedEventListener") MessageListener listener) {
        super(new ChannelTopic(topic), new MessageListenerAdapter(listener));
    }
}
