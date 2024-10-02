package faang.school.notificationservice.config.listener;

import faang.school.notificationservice.listeners.MentorshipAcceptedListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipAcceptedListenerConfig {

    @Value("${spring.data.redis.channel.mentorship_accepted_channel}")
    private String channelTopic;

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(channelTopic);
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedListenerAdapter(MentorshipAcceptedListener mentorshipAcceptedListener) {
        return new MessageListenerAdapter(mentorshipAcceptedListener);
    }

    @Bean
    public Pair<ChannelTopic, MessageListenerAdapter> mentorshipAcceptedListenerChannelPair(
            ChannelTopic mentorshipAcceptedTopic,
            MessageListenerAdapter mentorshipAcceptedListenerAdapter) {
        return Pair.of(mentorshipAcceptedTopic, mentorshipAcceptedListenerAdapter);
    }
}
