package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.ProfileViewEventListener;
import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.impl.UserFollowerEventListener;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channel.event_start}")
    private String eventStartTopic;

    @Value("${spring.data.redis.channel.profile-view}")
    private String profileViewChannel;


    @Value("${spring.data.redis.channel.follower}")
    private String userFollowerListener;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(EventStartEventListener eventStartEventListener,
                                                 ProfileViewEventListener profileViewEventListener,
                                                 UserFollowerEventListener userFollowerEventListener) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventStartListener(eventStartEventListener), eventStartTopic());
        container.addMessageListener(profileViewListener(profileViewEventListener), profileViewTopic());
        container.addMessageListener(userFollowerListener(userFollowerEventListener), userFollowerTopic());
        return container;
    }

    @Bean
    MessageListenerAdapter eventStartListener(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    ChannelTopic eventStartTopic() {
        return new ChannelTopic(eventStartTopic);
    }

    @Bean
    @Qualifier("profileViewListener")
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewChannel);
    }

    @Bean
    @Qualifier("userFollowerListener")
    MessageListenerAdapter userFollowerListener(UserFollowerEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic userFollowerTopic() {
        return new ChannelTopic(userFollowerListener);
    }
}
