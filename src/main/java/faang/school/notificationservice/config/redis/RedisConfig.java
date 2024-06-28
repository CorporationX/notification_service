package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.incoming.MessageEvent;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.mentorship_accepted_channel.name}")
    private String mentorshipAcceptedChannel;
    @Value("${spring.data.redis.channel.follower.name}")
    private String followerChanel;

    private final FollowerEventListener followerEventListener;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter mentorshipAcceptedListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(mentorshipAcceptedListener, mentorshipAcceptedTopic());
        container.addMessageListener(followerListener(), followerTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedListener(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper mapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(mapper, MessageEvent.class));
        return redisTemplate;
    }

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(mentorshipAcceptedChannel);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerChanel);
    }

    @Bean
    public MessageListenerAdapter followerListener() {
        return new MessageListenerAdapter(followerEventListener);
    }
}
