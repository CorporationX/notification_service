package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.LikePostEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.MentorshipOfferedEventListener;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.listener.UserFollowerEventListener;
import faang.school.notificationservice.listener.SkillAcquiredEventListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    private static final String DEFAULT_LISTENER_METHOD = "onMessage";

    @Value("${redis.channels.project-follower}")
    private String projectFollowerEventChannel;

    @Value("${redis.channels.like_post}")
    private String topicNameLikePost;

    @Value("${redis.channels.goal-completed}")
    private String goalCompletedEventChannel;

    @Value("${redis.channels.user-follower}")
    private String userFollowerEventChannel;

    @Value("${redis.channels.mentorship-accepted}")
    private String mentorshipAcceptedEventChannel;

    @Value("${redis.channels.comment_channel}")
    private String topicNameComment;

    @Value("${redis.channels.achievement}")
    private String topicNameAchievement;

    @Value("${redis.channels.skill-acquired}")
    private String skillAcquiredEventChannel;

    @Value("${redis.channels.mentorship-offered}")
    private String mentorshipOfferedEventChannel;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic projectFollowerChannelTopic() {
        return new ChannelTopic(projectFollowerEventChannel);
    }

    @Bean
    public ChannelTopic likePostChannelTopic() {
        return new ChannelTopic(topicNameLikePost);
    }

    @Bean
    public ChannelTopic userFollowerChannelTopic() {
        return new ChannelTopic(userFollowerEventChannel);
    }

    @Bean
    public ChannelTopic goalCompletedChannelTopic() {
        return new ChannelTopic(goalCompletedEventChannel);
    }

    @Bean
    public ChannelTopic mentorshipAcceptedChannelTopic() {
        return new ChannelTopic(mentorshipAcceptedEventChannel);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(topicNameComment);
    }

    @Bean
    public ChannelTopic achievementChannelTopic() {
        return new ChannelTopic(topicNameAchievement);
    }

    @Bean
    public ChannelTopic skillAcquiredChannelTopic() {
        return new ChannelTopic(skillAcquiredEventChannel);
    }

    @Bean
    public ChannelTopic mentorshipOfferedChannelTopic() {
        return new ChannelTopic(mentorshipOfferedEventChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ProjectFollowerEventListener projectFollowerEventListener,
            GoalCompletedEventListener goalCompletedEventListener,
            UserFollowerEventListener userFollowerEventListener,
            LikePostEventListener likePostEventListener,
            MentorshipAcceptedEventListener mentorshipAcceptedEventListener,
            CommentEventListener commentEventListener,
            AchievementEventListener achievementEventListener,
            SkillAcquiredEventListener skillAcquiredEventListener,
            MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(projectFollowerEventListenerAdapter(projectFollowerEventListener), projectFollowerChannelTopic());
        container.addMessageListener(likePostEventListenerAdapter(likePostEventListener), likePostChannelTopic());
        container.addMessageListener(userFollowerEventListenerAdapter(userFollowerEventListener), userFollowerChannelTopic());
        container.addMessageListener(goalCompletedEventListenerAdapter(goalCompletedEventListener), goalCompletedChannelTopic());
        container.addMessageListener(mentorshipAcceptedEventListenerAdapter(mentorshipAcceptedEventListener), mentorshipAcceptedChannelTopic());
        container.addMessageListener(commentEventListenerAdapter(commentEventListener), commentTopic());
        container.addMessageListener(achievementEventListenerAdapter(achievementEventListener), achievementChannelTopic());
        container.addMessageListener(skillAcquiredEventListenerAdapter(skillAcquiredEventListener), skillAcquiredChannelTopic());
        container.addMessageListener(mentorshipOfferedEventListenerAdapter(mentorshipOfferedEventListener), mentorshipOfferedChannelTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter projectFollowerEventListenerAdapter(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter likePostEventListenerAdapter(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter achievementEventListenerAdapter(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter userFollowerEventListenerAdapter(UserFollowerEventListener userFollowerEventListener) {
        return new MessageListenerAdapter(userFollowerEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter goalCompletedEventListenerAdapter(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedEventListenerAdapter(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter mentorshipOfferedEventListenerAdapter(MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener, DEFAULT_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter skillAcquiredEventListenerAdapter(SkillAcquiredEventListener skillAcquiredEventListener) {
        return new MessageListenerAdapter(skillAcquiredEventListener, DEFAULT_LISTENER_METHOD);
    }
}