package faang.school.notificationservice.config.redis.listeners;

import faang.school.notificationservice.listeners.MentorshipRequestListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipOfferedListenerConfig {
    @Bean
    public MessageListenerAdapter mentorshipOfferedListenerAdapter(MentorshipRequestListener mentorshipRequestListener) {
        return new MessageListenerAdapter(mentorshipRequestListener);
    }

    @Bean
    public ChannelTopic mentorshipOfferedTopic(@Value("${spring.data.redis.channel.mentorship_offered}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> postViewListenerChannelPair(
            @Qualifier("mentorshipOfferedListenerAdapter") MessageListenerAdapter adapter,
            @Qualifier("mentorshipOfferedTopic") ChannelTopic channelTopic) {

        return Pair.of(adapter, channelTopic);
    }
}
