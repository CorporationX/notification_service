package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.GoalCompleteEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@SpringBootTest
public class GoalCompleteEventListenerTest {

    @Autowired
    private GoalCompletedEventListener goalListener;

    @SpyBean
    private GoalCompletedEventListener spyGoalListener;

    @Autowired
    @Qualifier("redisTemplateForSending")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.goal}")
    private String topic;

    @DisplayName("Тестирование получения сообщения из Redis")
    @Test
    public void shouldListenMessageFromRedis() {
        GoalCompleteEvent goal = new GoalCompleteEvent(16L, 19L);

        doNothing().when(spyGoalListener).sendNotification(any(), any());

        redisTemplate.convertAndSend(topic, goal);

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(spyGoalListener).sendNotification(eq(19L), anyString())
                );
    }
}
