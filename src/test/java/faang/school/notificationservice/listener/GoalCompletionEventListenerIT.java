package faang.school.notificationservice.listener;

import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.dto.event.GoalCompletionNotificationEvent;

import static faang.school.notificationservice.model.dto.UserDto.PreferredContact.PHONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import faang.school.notificationservice.service.SmsService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

    @SpyBean
    private SmsService smsService;


    @Test
    public void testListenGoalCompletion() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserDto> captorDto = ArgumentCaptor.forClass(UserDto.class);
        UserDto dto = new UserDto(1, "test", "test", "test", PHONE);
        GoalCompletionNotificationEvent event = new GoalCompletionNotificationEvent(dto, "test");
        kafkaTestTemplate.send(goalTopic, event);


        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {

                    verify(smsService, times(1)).send(captorDto.capture(), captor.capture());
                    String receivedMsg = captor.getValue();

                    assertEquals("\"Congrats! You have achieved test goal!\"", receivedMsg);
                    assertEquals(captorDto.getValue().getId(), dto.getId());
                });
    }
}
