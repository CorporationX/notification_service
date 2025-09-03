package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.FollowerEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
public class FollowerEventListenerTest {

    @Autowired
    private FollowerEventListener eventListener;

    @SpyBean
    private FollowerEventListener spyEventListener;

    @Autowired
    @Qualifier("kafkaTemplateForSend")
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.follower}")
    private String topic;

    @DisplayName("Тестирование получения сообщения из Kafka")
    @Test
    public void shouldListenMessageFromKafka() {
        FollowerEvent eventDto = new FollowerEvent(
                5L,
                12L
        );

        doNothing().when(spyEventListener).sendNotification(any(), any());

        kafkaTemplate.send(topic, eventDto);

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(spyEventListener).sendNotification(eq(12L), anyString())
                );
    }
}
