package faang.school.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

/**
 * Конфигурационный класс для настройки подключения к Redis и работы с сообщениями.
 * <p>
 * Содержит настройки для:
 * <ul>
 *   <li>Подключения к Redis серверу</li>
 *   <li>Сериализации/десериализации данных</li>
 *   <li>Обработки сообщений из Redis каналов</li>
 * </ul>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    /**
     * Создает фабрику подключений к Redis.
     *
     * @return настроенная фабрика подключений
     * @implNote Использует настройки из {@link RedisProperties}:
     * <ul>
     *   <li>Хост и порт Redis сервера</li>
     *   <li>Таймауты подключения и чтения</li>
     * </ul>
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
     * @return настроенный RedisTemplate
     * @implNote Использует:
     * <ul>
     *   <li>StringRedisSerializer для ключей</li>
     *   <li>GenericJackson2JsonRedisSerializer для значений</li>
     * </ul>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(serializer);

        return template;
    }

    /**
     * Создает контейнер для обработки сообщений из Redis каналов.
     *
     * @param connectionFactory фабрика подключений к Redis
     * @param listeners         список слушателей, реализующих {@link MessageListener}
     * @return настроенный контейнер слушателей
     * @implNote Автоматически регистрирует слушатели, помеченные аннотацией {@link RedisChannel},
     * подписывая их на соответствующие каналы.
     */
    @Bean
    public RedisMessageListenerContainer listenerContainer(
            JedisConnectionFactory connectionFactory,
            List<MessageListener> listeners) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

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
}