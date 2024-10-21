package faang.school.notificationservice.config.redis.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedAcceptContainerMessageListener implements RedisContainerMessageListener {
    private final MessageListenerAdapter adapter;
    private final Topic topic;

    public MentorshipRequestedAcceptContainerMessageListener(MessageListener mentorshipRequestAcceptedListener,
                                                             Topic mentorshipRequestAcceptedTopicName) {
        this.adapter = new MessageListenerAdapter(mentorshipRequestAcceptedListener);
        this.topic = mentorshipRequestAcceptedTopicName;
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
