package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.EventStartEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
public class EventStartListenerTest {

    @Autowired
    private EventStartListener eventListener;

    @SpyBean
    private EventStartListener spyEventListener;

    @Autowired
    @Qualifier("redisTemplateForSending")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.event}")
    private String topic;

    @DisplayName("Тестирование получения сообщения из Redis")
    @Test
    public void shouldListenMessageFromRedis() {
        EventStartEvent eventDto = new EventStartEvent(
                "someTitle",
                new ArrayList<>(List.of(3L))
        );

        doNothing().when(spyEventListener).sendNotification(any(), any());

        redisTemplate.convertAndSend(topic, eventDto);

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(spyEventListener).sendNotification(eq(3L), anyString())
                );
    }
}

