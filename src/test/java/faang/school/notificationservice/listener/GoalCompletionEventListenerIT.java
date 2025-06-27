package faang.school.notificationservice.listener;

import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.service.notification.implimentation.SmsNotificationService;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(TestKafkaConfig.class)
@ContextConfiguration(initializers = IntegrationTestContextInitializer.class)
@SpringBootTest
public class GoalCompletionEventListenerIT {

    @Autowired
    private KafkaTemplate<String, GoalCompletionNotificationEvent> kafkaTestTemplate;

    @SpyBean
    private SmsNotificationService smsService;

    @Test
    public void testListenGoalCompletion() throws ExecutionException, InterruptedException {
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);

        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setPreference(PreferredContact.PHONE);
        GoalCompletionNotificationEvent event = new GoalCompletionNotificationEvent(owner, "test");

        kafkaTestTemplate.send("goal-completed", event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(smsService, times(1)).send(userCaptor.capture(), messageCaptor.capture());

                    UserDto user = userCaptor.getValue();
                    String receivedMsg = messageCaptor.getValue();

                    assertEquals(1L, user.getId());
                    assertEquals("\"Congrats! You have achieved test goal!\"", receivedMsg);
                });
    }
}
