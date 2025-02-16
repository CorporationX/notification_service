package faang.school.notificationservice.config.redis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public record ListenerRegistryItem(
        MessageListener messageListener,
        List<ChannelTopic> topics) {
}
