package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

public class EventListenerForTest extends AbstractEventListener<EventForTest>
        implements MessageListener, RedisContainerMessageListener {
    public EventListenerForTest(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                List<MessageBuilder<EventForTest>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return null;
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic("test_topic");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
    }
}
