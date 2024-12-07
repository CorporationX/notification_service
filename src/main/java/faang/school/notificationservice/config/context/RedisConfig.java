package faang.school.notificationservice.config.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.deserializer.LocalDateTimeArrayDeserializer;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.UnfollowEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        log.info("Настройка соединения с Redis: хост={}, порт={}", redisProperties.getHost(), redisProperties.getPort());
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
        try {
            factory.afterPropertiesSet();
            log.info("Соединение с Redis успешно создано.");
        } catch (Exception e) {
            log.error("Ошибка создания соединения с Redis: {}", e.getMessage());
        }
        return factory;
    }

    @Bean
    public RedisMessageListenerContainer container(LettuceConnectionFactory lettuceConnectionFactory,
                                                   MessageListenerAdapter followerListenerAdapter,
                                                   MessageListenerAdapter unfollowListenerAdapter) {
        log.info("Настройка RedisMessageListenerContainer...");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(followerListenerAdapter, new ChannelTopic(redisProperties.getFollowerChannel()));
        container.addMessageListener(unfollowListenerAdapter, new ChannelTopic(redisProperties.getUnfollowChannel()));
        log.info("RedisMessageListenerContainer успешно настроен для канала 'followerChannel'.");
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        log.info("Создание RedisTemplate для взаимодействия с Redis.");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapperForRedisConfig()));
        log.info("RedisTemplate успешно создан и настроен.");
        return redisTemplate;
    }

    @Bean
    public ObjectMapper objectMapperForRedisConfig() {
        log.info("Настройка ObjectMapper для поддержки LocalDateTime.");
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeArrayDeserializer());
        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("ObjectMapper успешно настроен.");
        return mapper;
    }

    @Bean
    public MessageListenerAdapter followerListenerAdapter(FollowerEventListener followerEventListener) {
        log.info("Настройка FollowerListenerAdapter для обработки сообщений...");
        return new MessageListenerAdapter(followerEventListener, "onMessage");
    }

    @Bean MessageListenerAdapter unfollowListenerAdapter(UnfollowEventListener unfollowEventListener) {
        log.info("Настройка UnfollowListenerAdapter для обработки сообщений...");
        return new MessageListenerAdapter(unfollowEventListener, "onMessage");
    }
}
