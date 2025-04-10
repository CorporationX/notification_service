package faang.school.notificationservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Конфигурационный класс для настройки подключения к Redis и работы с механизмом pub/sub.
 * Создает и настраивает бины для подключения к Redis, сериализации сообщений,
 * а также подписки на каналы с помощью слушателей сообщений.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private final RedisProperties redisProperties;

    /**
     * Создает фабрику подключений к Redis на основе конфигурационных свойств.
     *
     * @return фабрика подключений JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
                redisProperties.getHost(), redisProperties.getPort());
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(redisProperties.getConnectTimeout()))
                .readTimeout(Duration.ofMillis(redisProperties.getReadTimeout()))
                .build();
        return new JedisConnectionFactory(config, clientConfig);
    }

    /**
     * Создает и настраивает RedisTemplate для работы с Redis.
     *
     * @param factory фабрика подключений к Redis
     * @return настроенный экземпляр RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /**
     * Создает и настраивает контейнер для слушателей сообщений Redis.
     * Регистрирует все слушатели, аннотированные @RedisChannel, для соответствующих каналов.
     *
     * @param connectionFactory фабрика подключений
     * @param redisTaskExecutor исполнитель задач
     * @param redisSubscriptionExecutor исполнитель подписок
     * @return контейнер слушателей сообщений Redis
     */
    @Bean
    public RedisMessageListenerContainer listenerContainer(
            JedisConnectionFactory connectionFactory,
            List<MessageListener> listeners,
            Executor redisTaskExecutor,
            Executor redisSubscriptionExecutor) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(redisTaskExecutor);
        container.setSubscriptionExecutor(redisSubscriptionExecutor);

        for (MessageListener listener : listeners) {
            RedisChannel annotation = listener.getClass().getAnnotation(RedisChannel.class);
            if (annotation != null) {
                String channel = annotation.value();
                container.addMessageListener(new MessageListenerAdapter(listener), new ChannelTopic(channel));
                log.info("Registered {} on channel '{}'", listener.getClass().getSimpleName(), channel);
            }
        }

        return container;
    }

    /**
     * Создает исполнитель задач для обработки сообщений Redis.
     *
     * @return пул потоков для выполнения задач
     */
    @Bean
    @Primary
    public Executor redisTaskExecutor() {
        return Executors.newFixedThreadPool(redisProperties.getListener().getTaskThreads(),
                new NamedThreadFactory("redis-task-"));
    }

    /**
     * Создает исполнитель для обработки подписок Redis.
     *
     * @return пул потоков для обработки подписок
     */
    @Bean
    public Executor redisSubscriptionExecutor() {
        return Executors.newFixedThreadPool(redisProperties.getListener().getSubscriptionThreads(),
                new NamedThreadFactory("redis-sub-"));
    }
}