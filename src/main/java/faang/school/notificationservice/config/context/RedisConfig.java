package faang.school.notificationservice.config.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.messaging.listener.AchievementEventListener;

import faang.school.notificationservice.messaging.listener.FollowEventListener;
import faang.school.notificationservice.messaging.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.channel.follower}")
    private String followEventsTopic;

   /* @Value("${spring.data.redis.channel.achievement}")
    private String achievementEventsTopic;*/

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public FollowEventListener followEventListener(
            List<NotificationService> notifications,
            List<MessageBuilder<FollowEventDto>> messageBuilders,
            UserServiceClient userServiceClient) {
        return new FollowEventListener(notifications, messageBuilders, userServiceClient);
    }

    /*@Bean
    public MessageListenerAdapter followListenerAdapter(FollowEventListener followEventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(followEventListener, "onMessage");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        return adapter;
    }*/

    /*@Bean
    public MessageListenerAdapter followListenerAdapter(FollowEventListener followEventListener) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        MessageListenerAdapter adapter = new MessageListenerAdapter(followEventListener, "onMessage1");
        adapter.setSerializer(serializer);
        return adapter;
    }*/
    @Bean
    public MessageListenerAdapter followListenerAdapter(
            FollowEventListener followEventListener, ObjectMapper objectMapper) {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        Jackson2JsonRedisSerializer<FollowEventDto> serializer =
                new Jackson2JsonRedisSerializer<>(FollowEventDto.class);
        serializer.setObjectMapper(objectMapper);


        MessageListenerAdapter adapter = new MessageListenerAdapter(followEventListener, "onMessage1");
        adapter.setSerializer(serializer);
        return adapter;
    }

    @Bean
    public ChannelTopic followEventTopic() {
        return new ChannelTopic(followEventsTopic);
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter followListenerAdapter,
            ChannelTopic followEventTopic
            //MessageListenerAdapter achievementListenerAdapter,
            //ChannelTopic achievementEventTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(followListenerAdapter, followEventTopic);
        //container.addMessageListener(achievementListenerAdapter,achievementEventTopic);
        System.out.println("RedisMessageListenerContainer started for topic: " + followEventTopic.getTopic());
        return container;
    }

   /* @Bean
    public AchievementEventListener achievementEventListener(
            UserServiceClient userServiceClient,
            List<NotificationService> notifications) {
        return new AchievementEventListener(userServiceClient,notifications );
    }

    @Bean
    public MessageListenerAdapter achievementListenerAdapter(AchievementEventListener achievementEventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(achievementEventListener, "onMessage2");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        return adapter;
    }

    @Bean
    public ChannelTopic achievementEventTopic() {
        return new ChannelTopic(achievementEventsTopic);
    }*/
}
