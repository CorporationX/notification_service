package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.dto.RedisProperties;
import faang.school.notificationservice.listener.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    private final String methodName = "onMessage";

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic commentChannel() {
        return new ChannelTopic(redisProperties.getChannel().getComment());
    }

    @Bean
    public ChannelTopic recRequestChannel() {
        return new ChannelTopic(redisProperties.getChannel().getRecommendation());
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }

    @Bean
    public MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener, methodName);
    }

    @Bean
    MessageListenerAdapter followListenerAdapter(FollowEventListener followEventListener) {
        return new MessageListenerAdapter(followEventListener);
    }

    @Bean
    public MessageListenerAdapter eventStartListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    public ChannelTopic followTopic() {
        return new ChannelTopic(redisProperties.getChannel().getFollower());
    }

    @Bean
    ChannelTopic eventStartTopic() {
        return new ChannelTopic(redisProperties.getChannel().getEventStart());
    }

    @Bean
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public MessageListenerAdapter recRequestListenerAdapter(RecommendationRequestedEventListener recRequestEventListener) {
        return new MessageListenerAdapter(recRequestEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter commentListener,
                                                                       MessageListenerAdapter likeListenerAdapter,
                                                                       MessageListenerAdapter eventStartListenerAdapter,
                                                                       MessageListenerAdapter recRequestListenerAdapter,
                                                                       MessageListenerAdapter followListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventStartListenerAdapter, eventStartTopic());
        container.addMessageListener(commentListener, commentChannel());
        container.addMessageListener(likeListenerAdapter, likeTopic());
        container.addMessageListener(recRequestListenerAdapter, recRequestChannel());
        container.addMessageListener(followListenerAdapter, followTopic());
        return container;
    }
}
