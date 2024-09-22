package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.CommentEventListener;
import lombok.extern.slf4j.Slf4j;
import faang.school.notificationservice.listener.project.ProjectFollowerEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.comment}")
    private String commentChannelName;
    @Value("${spring.data.redis.channel.project_follower}")
    private String projectFollowerTopicName;
  
    private final CommentEventListener commentEventListener;
    private final ProjectFollowerEventListener projectFollowerEventListener;

    @Autowired
    public RedisConfig(CommentEventListener commentEventListener) {
        this.commentEventListener = commentEventListener;
    }
  
    @Autowired
    public RedisConfig(ProjectFollowerEventListener projectFollowerEventListener) {
        this.projectFollowerEventListener = projectFollowerEventListener;
    }

    @Bean
    MessageListenerAdapter commentEventListenerListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }
    
    @Bean
    MessageListenerAdapter projectFollowerListener(ProjectFollowerEventListener projectFollowerEventListener){
        return new MessageListenerAdapter(projectFollowerEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(commentEventListener, new ChannelTopic(commentChannelName));
        container.addMessageListener(projectFollowerListener(projectFollowerEventListener), projectFollowerTopic());

        return container;
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannelName);
    }
    
    @Bean
    ChannelTopic projectFollowerTopic(){
        return new ChannelTopic(projectFollowerTopicName);
    }

    
}
