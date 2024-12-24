package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.achievment.AchievementEventListener;
import faang.school.notificationservice.listener.mentorshipoffered.MentorshipOfferedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.util.Pair;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private Integer port;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(List<Pair<MessageListenerAdapter, ChannelTopic>> requesters,
                                                        JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        requesters.forEach(
                (requester) -> container.addMessageListener(requester.getFirst(), requester.getSecond())
        );
        return container;
    }

    @Bean
    public ChannelTopic mentorshipOfferedTopic() {
        return new ChannelTopic(redisProperties.getChannels().getMentorshipOfferedChannel().getName());
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(redisProperties.getChannels().getAchievementChannel().getName());
    }

    @Bean
    @Qualifier("mentorshipListener")
    public MessageListenerAdapter mentorshipOfferedListener(MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener);
    }

    @Bean
    @Qualifier("achievementListener")
    public MessageListenerAdapter achievementListener(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> mentorshipOfferedEventPair(@Qualifier("mentorshipListener") MessageListenerAdapter mentorshipOfferedListener) {
        return Pair.of(mentorshipOfferedListener, mentorshipOfferedTopic());
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> achievementEventPair(@Qualifier("achievementListener") MessageListenerAdapter achievementListener) {
        return Pair.of(achievementListener, achievementTopic());
    }
}