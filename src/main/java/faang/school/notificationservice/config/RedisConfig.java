package faang.school.notificationservice.config;


import faang.school.notificationservice.listener.MentorshipRequestListener;
import faang.school.notificationservice.messaging.CommentEventListener;
import faang.school.notificationservice.messaging.FollowerEventListener;
import faang.school.notificationservice.listener.SkillAcquiredEventListener;
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

    @Value("${spring.data.redis.topic}")
    private String followerTopic;


    @Value("${spring.data.redis.channel.skill_acquired}")
    private String skillAcquiredChannel;

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
        return template;
    }

    @Bean
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipRequestListenerConfig(MentorshipRequestListener mentorshipRequestListener) {
        return new MessageListenerAdapter(mentorshipRequestListener);
    }

    @Bean
    public MessageListenerAdapter skillAcquiredListener(SkillAcquiredEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter commentEventListener(CommentEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic commentTopic(@Value("${spring.data.redis.channel.comment}")
                                     String commentChannel) {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                        MessageListenerAdapter mentorshipRequestListenerConfig,
                                                        MessageListenerAdapter skillAcquiredListener,
                                                        MessageListenerAdapter commentEventListener,
                                                        ChannelTopic commentTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, topic());
        container.addMessageListener(mentorshipRequestListenerConfig, mentorshipRequestTopic());
        container.addMessageListener(skillAcquiredListener, skillAcquiredTopic());
        container.addMessageListener(commentEventListener, commentTopic);
        return container;
    }


    @Bean
    public ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic("mentorshipRequest_topic");
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(followerTopic);
    }

    @Bean
    public ChannelTopic skillAcquiredTopic() {
        return new ChannelTopic(skillAcquiredChannel);
    }
}
