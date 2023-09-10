package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.MentorshipRequestListener;
import faang.school.notificationservice.listener.SkillOfferedEventListener;
import faang.school.notificationservice.listener.event.CommentEventListener;
import faang.school.notificationservice.listener.event.EventStartListener;
import faang.school.notificationservice.listener.mentorship_event.MentorshipAcceptedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.event_channel.name}")
    private String eventChannel;
    @Value("${spring.data.redis.channels.mentorship_requested_channel.name}")
    private String mentorshipRequestChannel;
    @Value("${spring.data.redis.channels.comment_channels.name}")
    private String commentChannel;
    @Value("${spring.data.redis.channels.skill_channel.name}")
    private String skillOfferedChannel;
    @Value("${spring.data.redis.channels.mentorship}")
    private String channel;

    @Bean(name = "eventMessageListenerAdapter")
    public MessageListenerAdapter eventMessageListener(EventStartListener eventStartListener) {
        return new MessageListenerAdapter(eventStartListener);
    }

    @Bean(name = "mentorshipRequestListenerAdapter")
    public MessageListenerAdapter mentorshipRequestEventListener(MentorshipRequestListener mentorshipRequestListener) {
        return new MessageListenerAdapter(mentorshipRequestListener);
    }

    @Bean(name = "commentMessageListenerAdapter")
    public MessageListenerAdapter commentMessageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean(name = "skillOfferedListenerAdapter")
    public MessageListenerAdapter skillOfferedMessageListener(SkillOfferedEventListener skillOfferedEventListener) {
        return new MessageListenerAdapter(skillOfferedEventListener);
    }

    @Bean(name = "mentorshipAcceptedListenerAdapter")
    public MessageListenerAdapter mentorshipAcceptedListenerAdapter(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Crated redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory();
    }

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(channel);
    }

    @Bean
    public ChannelTopic eventTopic() {
        return new ChannelTopic(eventChannel);
    }

    @Bean
    public ChannelTopic mentorshipRequestTopic() {
        return new ChannelTopic(mentorshipRequestChannel);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    ChannelTopic skillOfferedTopic() {
        return new ChannelTopic(skillOfferedChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            @Qualifier("eventMessageListenerAdapter") MessageListenerAdapter eventMessageListener,
            @Qualifier("commentMessageListenerAdapter") MessageListenerAdapter commentEventListener,
            @Qualifier("mentorshipRequestListenerAdapter") MessageListenerAdapter mentorshipRequestEventListener,
            @Qualifier("mentorshipAcceptedListenerAdapter") MessageListenerAdapter mentorshipAcceptedEventListener,
            @Qualifier("skillOfferedListenerAdapter") MessageListenerAdapter skillOfferedEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventMessageListener, eventTopic());
        container.addMessageListener(mentorshipRequestEventListener, mentorshipRequestTopic());
        container.addMessageListener(commentEventListener, commentTopic());
        container.addMessageListener(mentorshipAcceptedEventListener, mentorshipAcceptedTopic());
        container.addMessageListener(skillOfferedEventListener, skillOfferedTopic());
        return container;
    }
}