package faang.school.notificationservice.config;

import faang.school.notificationservice.messaging.likepost.LikePostEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.like_post}")
    private String topicNameLikePost;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        log.info("redis host {}, port {} ", host, port);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public ChannelTopic likePostTopic() {
        return new ChannelTopic(topicNameLikePost);
    }

    @Bean
    public MessageListenerAdapter likePostListenerAdapter(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likePostEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(likePostEventListener, likePostTopic());
        return container;
    }

}
