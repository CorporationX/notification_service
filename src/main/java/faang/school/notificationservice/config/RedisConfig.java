package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.listener.ProfileViewListener;
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
    String host;
    @Value("${spring.data.redis.port}")
    int port;
    @Value("${spring.data.redis.channels.profile_view_channel.name}")
    String profileViewChannelName;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannelName);
    }

    @Bean
    MessageListenerAdapter messageListener(ProfileViewListener profileViewListener) {
        return new MessageListenerAdapter(profileViewListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener, profileViewTopic());
        return container;
    }
}
