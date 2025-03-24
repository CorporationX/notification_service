package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.TransferEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.channel.transfer}")
    private String transferTopicName;

    @Bean
    JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter transferSentEventMessageListener(TransferEventListener transferEventListener) {
        return new MessageListenerAdapter(transferEventListener);
    }

    @Bean
    ChannelTopic transferTopic() {
        return new ChannelTopic(transferTopicName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(List<MessageListenerAdapter> listenerAdapters,
                                                 List<ChannelTopic> topics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());

        IntStream.range(0, listenerAdapters.size()).forEach(i ->
                container.addMessageListener(listenerAdapters.get(i), topics.get(i))
        );
        return container;
    }
}
