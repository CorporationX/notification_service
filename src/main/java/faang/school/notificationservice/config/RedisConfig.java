package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
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
    @Value("${spring.data.redis.channels.mentorship_accepted_request_event_channel}")
    private String mentorshipAcceptedEventChannelName;
    @Value("${spring.data.redis.channels.like_channel}")
    private String likeChannelName;

    @Bean
    MessageListenerAdapter skillOfferListenerAdapter(SkillOfferListener skillOfferListener) {
        return new MessageListenerAdapter(skillOfferListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter recommendationRequestListenerAdapter(RecommendationRequestListener recommendationRequestListener) {
        return new MessageListenerAdapter(recommendationRequestListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter mentorshipAcceptedRequestListenerAdapter(MentorshipAcceptedEventListener eventListener) {
        return new MessageListenerAdapter(eventListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter likeListenerAdapter(LikeEventListener likeListener) {
        return new MessageListenerAdapter(likeListener, "onMessage");
    }


    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter skillOfferListenerAdapter,
                                                        MessageListenerAdapter recommendationRequestListenerAdapter,
                                                        MessageListenerAdapter likeListenerAdapter,
                                                        MessageListenerAdapter mentorshipAcceptedRequestListenerAdapter ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(skillOfferListenerAdapter, topicInviteEvent());
        container.addMessageListener(recommendationRequestListenerAdapter, topicRecommendationRequestedEvent());
        container.addMessageListener(likeListenerAdapter, topicLikeEvent());
        container.addMessageListener(mentorshipAcceptedRequestListenerAdapter, topicMentorshipAcceptedRequestEvent());
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

    private ChannelTopic topicMentorshipAcceptedRequestEvent() {
        return new ChannelTopic(mentorshipAcceptedEventChannelName);
    }

    private ChannelTopic topicLikeEvent() {
        return new ChannelTopic(likeChannelName);
    }
}
