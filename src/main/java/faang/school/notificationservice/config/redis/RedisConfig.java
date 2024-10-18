package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.MentorshipOfferedEventListener;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.listener.RecommendationReceivedEventListener;
import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import faang.school.notificationservice.listener.SKillAcquiredEventListener;
import faang.school.notificationservice.listener.SkillOfferedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.like}")
    private String likeChannelTopic;

    @Value("${spring.data.redis.channel.event-starter}")
    private String eventStarter;

    @Value("${spring.data.redis.channel.mentorship-accepted-event-channel}")
    private String mentorshipAcceptedEventTopic;

    @Value("${spring.data.redis.channel.recommendation-request-channel}")
    private String recommendationReqTopic;

    @Value("${spring.data.redis.channel.mentorship-offered}")
    private String mentorshipOfferedTopic;

    @Value("${spring.data.redis.channel.recommendation-received}")
    private String recommendationReceived;

    @Value("${spring.data.redis.channel.follow-project}")
    private String followProjectTopic;

    @Value("${spring.data.redis.channel.comment-receiving.name}")
    private String commentReceivingTopic;

    @Value("${spring.data.redis.channel.comment-receiving.goal-completed-event-channel.name}")
    private String goalCompletedEvent;

    @Value("${spring.data.redis.channel.follower-event-channel}")
    private String followerEventChannel;

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementEventTopic;

    @Value("${spring.data.redis.channel.skill-acquired}")
    private String skillAcquired;

    @Value("${spring.data.redis.channel.skill-offered}")
    private String skillOffered;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter eventStartListenerAdapter,
                                                        MessageListenerAdapter mentorshipOfferedEventListenerAdapter,
                                                        MessageListenerAdapter recommendationReceivedListenerAdapter,
                                                        MessageListenerAdapter projectFollowerListenerAdapter,
                                                        MessageListenerAdapter likeListenerAdapter,
                                                        MessageListenerAdapter commentReceivingListenerAdapter,
                                                        MessageListenerAdapter goalCompletedListener,
                                                        MessageListenerAdapter followerEventListenerAdapter,
                                                        MessageListenerAdapter achievementEventListenerAdapter,
                                                        MessageListenerAdapter skillAcquiredListenerAdapter,
                                                        MessageListenerAdapter recommendationRequestedEventListenerAdapter,
                                                        MessageListenerAdapter mentorshipAcceptedEventListenerAdapter,
                                                        MessageListenerAdapter skillOfferedListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(eventStartListenerAdapter, eventStarter());
        container.addMessageListener(mentorshipOfferedEventListenerAdapter, mentorshipOffered());
        container.addMessageListener(recommendationReceivedListenerAdapter, recommendationReceived());
        container.addMessageListener(projectFollowerListenerAdapter, followProjectTopic());
        container.addMessageListener(likeListenerAdapter, likeTopic());
        container.addMessageListener(commentReceivingListenerAdapter, commentTopic());
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());
        container.addMessageListener(followerEventListenerAdapter, followerEventChannel());
        container.addMessageListener(achievementEventListenerAdapter, achievementTopic());
        container.addMessageListener(skillAcquiredListenerAdapter, skillAcquired());
        container.addMessageListener(recommendationRequestedEventListenerAdapter, recommendationRequestedEventTopic());
        container.addMessageListener(mentorshipAcceptedEventListenerAdapter, mentorshipAcceptedEventTopic());
        container.addMessageListener(skillOfferedListenerAdapter, skillOfferedEventTopic());

        return container;
    }

    @Bean
    public MessageListenerAdapter eventStartListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipOfferedEventListenerAdapter(MentorshipOfferedEventListener
                                                                                mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener);
    }


    @Bean
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public MessageListenerAdapter commentReceivingListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public MessageListenerAdapter followerEventListenerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter achievementEventListenerAdapter(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public MessageListenerAdapter skillAcquiredListenerAdapter(SKillAcquiredEventListener
                                                                       sKillAcquiredEventListener) {
        return new MessageListenerAdapter(sKillAcquiredEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationRequestedEventListenerAdapter(
            RecommendationRequestedEventListener recommendationRequestedEventListener) {
        return new MessageListenerAdapter(recommendationRequestedEventListener);
    }

    @Bean
    public MessageListenerAdapter projectFollowerListenerAdapter(ProjectFollowerEventListener
                                                                         projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener);
    }

    @Bean
    public MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedEventListenerAdapter(MentorshipAcceptedEventListener
                                                                                 mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationReceivedListenerAdapter(RecommendationReceivedEventListener
                                                                                recommendationReceivedEventListener) {
        return new MessageListenerAdapter(recommendationReceivedEventListener);
    }

    @Bean
    public MessageListenerAdapter skillOfferedListenerAdapter(SkillOfferedEventListener
                                                                      skillOfferedEventListener) {
        return new MessageListenerAdapter(skillOfferedEventListener);
    }

    @Bean
    public ChannelTopic mentorshipAcceptedEventTopic() {
        return new ChannelTopic(mentorshipAcceptedEventTopic);
    }

    @Bean
    public ChannelTopic eventStarter() {
        return new ChannelTopic(eventStarter);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentReceivingTopic);
    }

    @Bean
    public ChannelTopic mentorshipOffered() {
        return new ChannelTopic(mentorshipOfferedTopic);
    }

    @Bean
    public ChannelTopic recommendationReceived() {
        return new ChannelTopic(recommendationReceived);
    }

    @Bean
    public ChannelTopic followProjectTopic() {
        return new ChannelTopic(followProjectTopic);
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannelTopic);
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedEvent);
    }

    @Bean
    public ChannelTopic followerEventChannel() {
        return new ChannelTopic(followerEventChannel);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementEventTopic);
    }

    @Bean
    public ChannelTopic skillAcquired() {
        return new ChannelTopic(skillAcquired);
    }

    @Bean
    public ChannelTopic recommendationRequestedEventTopic() {
        return new ChannelTopic(recommendationReqTopic);
    }

    @Bean
    public ChannelTopic skillOfferedEventTopic() {
        return new ChannelTopic(skillOffered);
    }
}
