package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.RecommendationReceivedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final RecommendationReceivedEventListener recommendationReceivedEventListener;
    private final LikeEventListener likeEventListener;
    private final CommentEventListener commentEventListener;
    private final MentorshipAcceptedEventListener mentorshipAcceptedEventListener;
    private final GoalCompletedEventListener goalCompletedEventListener;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        log.info("Lettuce client for Redis is configured: host = {}, port = {}", redisProperties.getHost(), redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(redisProperties.getChannel().getRecommendation());
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }
    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(redisProperties.getChannel().getComment());
    }

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(redisProperties.getChannel().getMentorshipAcceptedChannel());
    }

    @Bean
    public ChannelTopic goalTopic() {
        return new ChannelTopic(redisProperties.getChannel().getGoal());
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ChannelTopic recommendationTopic,
            ChannelTopic likeTopic,
            ChannelTopic goalTopic,
            ChannelTopic mentorshipAcceptedTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(recommendationReceivedEventListener, recommendationTopic);
        container.addMessageListener(likeEventListener, likeTopic);
        container.addMessageListener(commentEventListener, commentTopic());
        container.addMessageListener(mentorshipAcceptedEventListener, mentorshipAcceptedTopic);
        container.addMessageListener(goalCompletedEventListener, goalTopic);
        log.info("RedisMessageListenerContainer is configured and listening to channels");
        return container;
    }
}