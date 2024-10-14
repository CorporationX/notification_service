package faang.school.notificationservice.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener {

    @Qualifier("followerTopic")
    private ChannelTopic topic;

    public FollowerEventListener(@Qualifier("followerTopic") ChannelTopic topic) {
        this.topic = topic;
    }


}
