package faang.school.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import faang.school.notificationservice.messaging.FollowerEventListener;
import faang.school.notificationservice.messaging.ProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.follower}")
    private String followerTopicName;
    @Value("${spring.data.redis.channel.profile_view}")
    private String profileViewTopicName;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerTopicName);
    }

    @Bean
    ChannelTopic profileViewTopic() {
        return new ChannelTopic(profileViewTopicName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter profileViewListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(profileViewListener, profileViewTopic());
        return container;
    }
}
