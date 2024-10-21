package faang.school.notificationservice.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean(value = "mentorshipEventTopic")
    public ChannelTopic mentorshipTopic(
            @Value("${spring.data.redis.channels.mentorship-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean(value = "mentorshipAcceptedMessageListenerAdapted")
    public MessageListenerAdapter mentorshipAcceptedMessageListenerAdapter(
            @Qualifier("mentorshipAcceptedEventListener") MessageListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer container(
            @Qualifier("mentorshipAcceptedMessageListenerAdapted")
            MessageListenerAdapter mentorshipAcceptedMessageListenerAdapter,
            @Qualifier("mentorshipEventTopic") ChannelTopic mentorshipTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipAcceptedMessageListenerAdapter, mentorshipTopic);
        return container;
    }
}
