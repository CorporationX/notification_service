package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.listener.TariffRateChangeListener;
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
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.project-follower-channel.name}")
    private String projectFollowerName;

    @Value("${spring.data.redis.channels.comment.name}")
    private String commentChannelName;

    @Value("${spring.data.redis.channels.tariff_rate_change_channel.name}")
    private String tariffRateChangeChannelName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(MessageListenerAdapter projectFollowerListener,
                                                           MessageListenerAdapter commentListener,
                                                           MessageListenerAdapter tariffRateListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(projectFollowerListener, projectFollowerTopic());
        container.addMessageListener(commentListener, commentTopic());
        container.addMessageListener(tariffRateListener, tariffRateChangeTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter projectFollowerListener(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener);
    }

    @Bean
    public MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean MessageListenerAdapter tariffRateListener(TariffRateChangeListener tariffRateChangeListener){
        return new MessageListenerAdapter(tariffRateChangeListener);
    }

    @Bean
    public ChannelTopic projectFollowerTopic() {
        return new ChannelTopic(projectFollowerName);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannelName);
    }

    @Bean
    public ChannelTopic tariffRateChangeTopic(){
        return new ChannelTopic(tariffRateChangeChannelName);
    }
}