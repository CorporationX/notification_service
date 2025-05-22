package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.RequestEventEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RedisConfiguration {
    private final RedisConfig redisConfig;

    @Bean
    @SuppressWarnings("unused")
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisConfig.getHost(), redisConfig.getPort());
    }

    @Bean(name = "requestEventsRedisTemplate")
    @SuppressWarnings("unused")
    public RedisMessageListenerContainer requestEventsRedisTemplate(
            RedisConnectionFactory connectionFactory,
            RequestEventEventListener requestEventEventListener,
            @Qualifier("requestEventsTopic") ChannelTopic requestEventsTopic) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(requestEventEventListener, requestEventsTopic);

        return container;
    }

    @Bean(name = "requestEventsTopic")
    @SuppressWarnings("unused")
    ChannelTopic requestEventsTopic() {
        return new ChannelTopic(redisConfig.getChannels().get("request_events_channel").name());
    }
}
