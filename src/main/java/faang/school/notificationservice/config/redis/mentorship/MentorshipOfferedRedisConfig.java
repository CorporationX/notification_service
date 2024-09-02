package faang.school.notificationservice.config.redis.mentorship;

import faang.school.notificationservice.listener.mentorship.MentorshipOfferedEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class MentorshipOfferedRedisConfig {
    @Value("${spring.data.redis.channel.mentorship_offered}")
    private String mentorshipOfferedChannelName;

    @Bean
    MessageListenerAdapter mentorshipOfferedListener(MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> mentorshipOffered(@Qualifier("mentorshipOfferedListener") MessageListenerAdapter mentorshipOfferedListener) {
        return Pair.of(mentorshipOfferedListener, new ChannelTopic(mentorshipOfferedChannelName));
    }
}
