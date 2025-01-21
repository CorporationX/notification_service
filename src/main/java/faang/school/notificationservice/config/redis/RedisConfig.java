package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.recommendation.RecommendationReceivedEventListener;
import faang.school.notificationservice.listener.subscription.FollowerEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
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
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.recommendation-requested}")
    private String recommendationRequestedChannel;

    @Value("${spring.data.redis.channel.recommendation-received}")
    private String recommendationReceivedChannel;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter recommendationReceivedListener(RecommendationReceivedEventListener recommendationReceivedEventListener) {
        return new MessageListenerAdapter(recommendationReceivedEventListener);
    }

    @Bean
    public ChannelTopic recommendationReceivedTopic() {
        return new ChannelTopic(recommendationReceivedChannel);
    }

    @Bean
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationReceivedListener,
                                                        MessageListenerAdapter followerListener) {
    public ChannelTopic recommendationRequestedTopic() {
        return new ChannelTopic(recommendationRequestedChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationReceivedListener,
                                                        MessageListener recommendationRequestedEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationReceivedListener, recommendationReceivedTopic());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(recommendationRequestedEventListener, recommendationRequestedTopic());
        return container;
    }
}
