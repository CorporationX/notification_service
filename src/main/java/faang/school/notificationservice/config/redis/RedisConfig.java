package faang.school.notificationservice.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisMessageListenerContainer container(List<ChannelListenerAdapter> channelListenerAdapters) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        channelListenerAdapters.forEach(
                listener -> container.addMessageListener(listener.getListenerAdapter(), listener.getTopic()));
        return container;
    }

    @Bean
    ChannelListenerAdapter commentChannelListenerAdapter(
            @Value("${spring.data.redis.channels.comment-channel.name}") String topic,
            @Qualifier("commentEventListener") MessageListener listener) {
        return new ChannelListenerAdapter(listener, topic);
    }

    @Bean
    ChannelListenerAdapter eventChannelListenerAdapter(
            @Value("${spring.data.redis.channels.comment-channel.name}") String topic,
            @Qualifier("commentEventListener") MessageListener listener) {
        return new ChannelListenerAdapter(listener, topic);
    }

    @Bean
    ChannelListenerAdapter goalChannelListenerAdapter(
            @Value("${spring.data.redis.channels.goal-channel.name}") String topic,
            @Qualifier("goalCompletedEventListener") MessageListener listener) {
        return new ChannelListenerAdapter(listener, topic);
    }

    @Bean
    ChannelListenerAdapter likeChannelListenerAdapter(
            @Value("${spring.data.redis.channels.like-channel.name}") String topic,
            @Qualifier("likeEventListener") MessageListener listener) {
        return new ChannelListenerAdapter(listener, topic);
    }

    @Bean
    ChannelListenerAdapter mentorshipChannelListenerAdapter(
            @Value("${spring.data.redis.channels.mentorship-channel.name}") String topic,
            @Qualifier("mentorshipAcceptedEventListener") MessageListener listener) {
        return new ChannelListenerAdapter(listener, topic);
    }
}