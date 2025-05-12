package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.subscription.FollowerEventListener;
import faang.school.notificationservice.listener.subscription.UnfollowerEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.data.redis.jedis.pool.max-active}")
    private Integer maxTotal;

    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private Integer minIdle;

    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private Long maxWaitMillis;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        log.info("Configuring Redis connection: host={}, port={}", redisProperties.getHost(), redisProperties.getPort());
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
        try {
            factory.afterPropertiesSet();
            log.info("Successfully created Redis connection");
        } catch (Exception e) {
            log.error("Error creating Redis connection: {}", e.getMessage());
            throw new IllegalStateException("Unable to establish Redis connection", e);
        }
        return factory;
    }

    @Bean
    public RedisMessageListenerContainer container(LettuceConnectionFactory lettuceConnectionFactory,
                                                   MessageListenerAdapter followerListenerAdapter,
                                                   MessageListenerAdapter unfollowerListenerAdapter) {
        log.info("Configuring RedisMessageListenerContainer...");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(followerListenerAdapter, new ChannelTopic(redisProperties.getChannel().getFollower()));
        container.addMessageListener(unfollowerListenerAdapter, new ChannelTopic(redisProperties.getChannel().getUnfollower()));
        log.info("RedisMessageListenerContainer successfully configured for channels");
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> subscriptionRedisTemplate() {
        log.info("Creating RedisTemplate for Redis interaction");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        log.info("RedisTemplate successfully created and configured");
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter followerListenerAdapter(FollowerEventListener followerEventListener,
                                                          ObjectMapper objectMapper) {
        log.info("Configuring FollowerListenerAdapter...");
        MessageListenerAdapter adapter = new MessageListenerAdapter(followerEventListener, "onMessage");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return adapter;
    }

    @Bean
    public MessageListenerAdapter unfollowerListenerAdapter(UnfollowerEventListener unfollowerEventListener,
                                                            ObjectMapper objectMapper) {
        log.info("Configuring UnfollowerListenerAdapter...");
        MessageListenerAdapter adapter = new MessageListenerAdapter(unfollowerEventListener, "onMessage");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return adapter;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
