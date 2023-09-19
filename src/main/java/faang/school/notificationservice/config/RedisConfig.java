package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.FolowerEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    //@Value("${spring.data.redis.port}")
    private final int redisPort = 6379;

   // @Value("${spring.data.redis.host}")
    private final String redisHost = "localhost" ;

   // @Value("${spring.data.redis.channel.follower}")
    private  final String followerChannelName = "follower_channel";

    //private  final String followerChannelName = "achievement_channel";

    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost,redisPort);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter folowerListener(FolowerEventListener folowerEventListener){
        return new MessageListenerAdapter(folowerEventListener);
    }

    @Bean
    ChannelTopic folllowerTopic(){
        return new ChannelTopic(followerChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(FolowerEventListener folowerEventListener, JedisConnectionFactory jedisConnectionFactory){
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();
    //    container.setMaxSubscriptionRegistrationWaitingTime(30000L);
        container.setConnectionFactory(jedisConnectionFactory);
        //container.setTopicSerializer(new StringRedisSerializer());
        container.addMessageListener(folowerListener(folowerEventListener),folllowerTopic());
        return container;
    }




}
