package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.MentorshipOfferedListener;
import faang.school.notificationservice.listener.RecommendationRequestListener;
import faang.school.notificationservice.listener.SkillOfferListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.skill_offer_channel}")
    private String skillOfferChannelName;
    @Value("${spring.data.redis.channels.recommendation_requested_event_channel}")
    private String recommendationRequestedEventChannelName;
    @Value("${spring.data.redis.channels.mentorship_offered_channel}")
    private String mentorship_offered_channel;

    @Bean
    MessageListenerAdapter skillOfferListenerAdapter(SkillOfferListener skillOfferListener) {
        return new MessageListenerAdapter(skillOfferListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter recommendationRequestListenerAdapter(RecommendationRequestListener recommendationRequestListener) {
        return new MessageListenerAdapter(recommendationRequestListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter mentorshipOfferedListenerAdapter(MentorshipOfferedListener mentorshipOfferedListener) {
        return new MessageListenerAdapter(mentorshipOfferedListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter skillOfferListenerAdapter,
                                                        MessageListenerAdapter recommendationRequestListenerAdapter,
                                                        MessageListenerAdapter mentorshipOfferedListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(skillOfferListenerAdapter, topicInviteEvent());
        container.addMessageListener(recommendationRequestListenerAdapter, topicRecommendationRequestedEvent());
        container.addMessageListener(mentorshipOfferedListenerAdapter, topicMentorshipOffered());

        return container;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    private ChannelTopic topicInviteEvent() {
        return new ChannelTopic(skillOfferChannelName);
    }

    private ChannelTopic topicRecommendationRequestedEvent() {
        return new ChannelTopic(recommendationRequestedEventChannelName);
    }

    private ChannelTopic topicMentorshipOffered() {
        return new ChannelTopic(mentorship_offered_channel);
    }
}