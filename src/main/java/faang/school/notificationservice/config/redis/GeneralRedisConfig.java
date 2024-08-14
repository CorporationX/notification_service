package faang.school.notificationservice.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

import java.util.List;

@Configuration
public class GeneralRedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory jedisConnectionFactory,
                                                        List<Pair<MessageListenerAdapter, ChannelTopic>> redisEventListener) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        for (var listenerWithTopic : redisEventListener) {
            container.addMessageListener(listenerWithTopic.getFirst(), listenerWithTopic.getSecond());
        }
        return container;
    }

}
