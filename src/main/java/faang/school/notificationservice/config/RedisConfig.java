package faang.school.notificationservice.config;

import faang.school.notificationservice.messaging.AchievementListener;
import faang.school.notificationservice.messaging.MentorshipOfferedEventListener;
import faang.school.notificationservice.messaging.RecommendationEventListener;
import faang.school.notificationservice.messaging.SkillOfferedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.achievement}")
    private String achievementTopic;
    @Value("${spring.data.redis.channels.mentorship_offered_event.name}")
    private String mentorshipOfferedEvent;
    @Value("${spring.data.redis.channels.skill-event.skill-offered-channel}")
    String skillOfferedChannel;
    @Value("${spring.data.redis.channels.recommendation_channel.name}")
    private String recommendationChannel;

    private final RecommendationEventListener recommendationEventListener;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        System.out.println(port);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementTopic);
    }

    @Bean
    public MessageListenerAdapter achievementAdapter(AchievementListener achievementListener) {
        return new MessageListenerAdapter(achievementListener);
    }

    @Bean
    MessageListenerAdapter mentorshipOfferedAdapter(MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener);
    }

    @Bean
    MessageListenerAdapter skillOfferedAdapter(SkillOfferedEventListener skillOfferedEventListener) {
        return new MessageListenerAdapter(skillOfferedEventListener);
    }

    @Bean
    MessageListenerAdapter recommendationAdapter(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    ChannelTopic mentorshipOfferedEvent() {
        return new ChannelTopic(mentorshipOfferedEvent);
    }

    @Bean
    ChannelTopic recommendationChannel() {
        return new ChannelTopic(recommendationChannel);
    }


    @Bean
    RedisMessageListenerContainer redisContainer(
            MessageListenerAdapter achievementAdapter,
            MessageListenerAdapter mentorshipOfferedAdapter,
            MessageListenerAdapter skillOfferedAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(achievementAdapter, achievementTopic());
        container.addMessageListener(mentorshipOfferedAdapter, mentorshipOfferedEvent());
        container.addMessageListener(skillOfferedAdapter, skillOfferedChannel());

        MessageListenerAdapter recommendation = new MessageListenerAdapter(recommendationEventListener);
        container.addMessageListener(recommendation, recommendationChannel());
        return container;
    }

    ChannelTopic skillOfferedChannel() {
        return new ChannelTopic(skillOfferedChannel);
    }
}
