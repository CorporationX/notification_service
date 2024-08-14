package faang.school.notificationservice.config.redis.mentorship;

import faang.school.notificationservice.listener.mentorship.MentorshipAcceptedEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipRedisConfig {

    @Value("${spring.data.redis.channel.mentorship_accepted}")
    private String mentorshipAcceptedChannelName;

    @Bean
    MessageListenerAdapter mentorshipAcceptedListener(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> mentorshipAccepted(@Qualifier("mentorshipAcceptedListener") MessageListenerAdapter mentorshipAcceptedListener) {
        return Pair.of(mentorshipAcceptedListener, new ChannelTopic(mentorshipAcceptedChannelName));
    }
}
