package faang.school.notificationservice.listener;

import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.dto.event.GoalCompletionNotificationEvent;
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
import java.util.Locale;
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

    @Autowired
    private GoalCompletionEventListener listener;


    @Test
    public void testListenGoalCompletion() {
        ArgumentCaptor<GoalCompletionNotificationEvent> captor = ArgumentCaptor.forClass(GoalCompletionNotificationEvent.class);
        GoalCompletionNotificationEvent event = new GoalCompletionNotificationEvent(new UserDto(), "test");
        kafkaTestTemplate.send(goalTopic, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {

                    verify(listener, times(1)).getMessage(captor.capture(), Locale.getDefault());
                    String receivedMsg = captor.getValue().getGoalTitle();

                    assertEquals("\"Congrats! You have achieved test goal!\"", receivedMsg);
                });
    }
}
