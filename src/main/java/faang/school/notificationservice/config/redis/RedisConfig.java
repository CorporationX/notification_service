package faang.school.notificationservice.config.redis;

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
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    private final RedisProperties redisProperties;

    @Value("${spring.data.redis.port}")
    private int port;
    private final String methodName = "onMessage";

    @Value("${spring.data.redis.channel.eventStart-event-channel}")
    private String eventChannel;
    @Bean
    public ChannelTopic commentChannel() {
        return new ChannelTopic(redisProperties.getChannel().getComment());
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener, methodName);
    MessageListenerAdapter eventStartListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    ChannelTopic eventStartTopic() {
        return new ChannelTopic(eventChannel);
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter eventStartListenerAdapter) {
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter commentListener,
                                                                       MessageListenerAdapter likeListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(eventStartListenerAdapter, eventStartTopic());
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentListener, commentChannel());
        container.addMessageListener(likeListenerAdapter, likeTopic());
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
