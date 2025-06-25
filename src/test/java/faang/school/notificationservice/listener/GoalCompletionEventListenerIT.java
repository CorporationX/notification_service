package faang.school.notificationservice.listener;

import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.GoalCompletionNotificationEvent;
import faang.school.notificationservice.service.SmsService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(TestKafkaConfig.class)
@ContextConfiguration(initializers = IntegrationTestContextInitializer.class)
@SpringBootTest
@EnableKafka
public class GoalCompletionEventListenerIT {

    @Value("${spring.kafka.topics.goal-completed-topic.name}")
    private String goalTopic;

    @Autowired
    private KafkaTemplate<String, GoalCompletionNotificationEvent> kafkaTestTemplate;

    @MockBean
    private SmsService smsService;

    @Test
    public void testListenGoalCompletion() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        GoalCompletionNotificationEvent event = new GoalCompletionNotificationEvent(new UserDto(), "test");
        kafkaTestTemplate.send(goalTopic, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(smsService, times(1)).sendSms(captor.capture());
                    String receivedMsg = captor.getValue();

                    assertEquals("\"Congrats! You have achieved test goal!\"", receivedMsg);
                });
    }
}
