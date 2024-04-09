package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.CreateRequestEventListener;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.RecommendationRequestListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channel.follower}")
    private String followerChannelName;
    @Value("${spring.data.redis.channel.recommendationRequest}")
    private String recommendationRequestedChannelName;
    @Value("${spring.data.redis.channel.create_request}")
    private String createRequestChannelName;

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
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter recommendationRequestedListener(RecommendationRequestListener recommendationRequestedEventListener) {
        return new MessageListenerAdapter(recommendationRequestedEventListener);
    }

    @Bean
    MessageListenerAdapter createRequestListener(CreateRequestEventListener createRequestEventListener) {
        return new MessageListenerAdapter(createRequestEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerChannelName);
    }

    @Bean
    ChannelTopic recommendationRequestedTopic() {
        return new ChannelTopic(recommendationRequestedChannelName);
    }

    @Bean
    ChannelTopic createRequestTopic() {
        return new ChannelTopic(createRequestChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(FollowerEventListener followerListener,
                                                 RecommendationRequestListener recommendationRequestedListener,
                                                 CreateRequestEventListener createRequestListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(recommendationRequestedListener, recommendationRequestedTopic());
        container.addMessageListener(createRequestListener, createRequestTopic());
        return container;
    }
}
