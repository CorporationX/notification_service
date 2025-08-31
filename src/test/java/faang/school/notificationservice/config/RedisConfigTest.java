package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


class RedisConfigTest {

    private RedisConfig redisConfig;
    private RecommendationRequestedEventListener listener;

    @BeforeEach
    void setUp() {
        listener = mock(RecommendationRequestedEventListener.class);
        redisConfig = new RedisConfig(listener);
    }

    @Test
    void recommendationRequestedTopic_shouldReturnCorrectTopic() {
        ChannelTopic topic = redisConfig.recommendationRequestedTopic();
        assertNotNull(topic);
        assertEquals("recommendation-requested", topic.getTopic());
    }

    @Test
    void redisContainer_shouldCreateContainerWithListener() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        RedisMessageListenerContainer container = redisConfig.redisContainer(connectionFactory);

        assertNotNull(container);
        assertEquals(connectionFactory, container.getConnectionFactory());
    }
}