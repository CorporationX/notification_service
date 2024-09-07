package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.project.ProjectFollowerEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    private final ProjectFollowerEventListener projectFollowerEventListener;

    @Value("${spring.data.redis.channel.project_follower}")
    private String projectFollowerTopicName;

    @Autowired
    public RedisConfig(ProjectFollowerEventListener projectFollowerEventListener) {
        this.projectFollowerEventListener = projectFollowerEventListener;
    }


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(projectFollowerListener(projectFollowerEventListener), projectFollowerTopic());

        return container;
    }

    @Bean
    ChannelTopic projectFollowerTopic(){
        return new ChannelTopic(projectFollowerTopicName);
    }

    @Bean
    MessageListenerAdapter projectFollowerListener(ProjectFollowerEventListener projectFollowerEventListener){
        return new MessageListenerAdapter(projectFollowerEventListener);
    }
}
