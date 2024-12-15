package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.profile.ProfileViewEventListener;
import faang.school.notificationservice.listener.recommendation.RecommendationReceivedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Value("${spring.data.redis.channel.profile_view}")
    private String profileViewChannel;


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
    MessageListenerAdapter profileViewEventListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    public ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannel);
    }

    @Bean
    ChannelTopic profileViewTopic(){
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationReceivedListener,
                                                        MessageListenerAdapter profileViewEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationReceivedListener, recommendationTopic());
        container.addMessageListener(profileViewEventListener, profileViewTopic());
        return container;
    }
}
