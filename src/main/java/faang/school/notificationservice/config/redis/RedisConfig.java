package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.service.listener.EventStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value( "${spring.data.redis.channel.event-start-channel}" )
    private  String eventStartChannel;


    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration( host, port );
        return new JedisConnectionFactory( config );
    }

    @Bean
    MessageListenerAdapter startEventListener(EventStartEventListener startEventListener) {
        return new MessageListenerAdapter(startEventListener);
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(eventStartChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener, topic());
        return container;
    }

}
