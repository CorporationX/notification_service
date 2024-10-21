package faang.school.notificationservice.config.redis.channel.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Getter
@RequiredArgsConstructor
public abstract class AbstractChannelListenerAdapter {
    protected final ChannelTopic topic;
    protected final MessageListenerAdapter listenerAdapter;
}
