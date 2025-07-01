package faang.school.notificationservice.listener;

import faang.school.notificationservice.NotificationServiceApp;
import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.service.notification.implimentation.EmailNotificationService;
import faang.school.notificationservice.service.notification.implimentation.SmsNotificationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Import(TestKafkaConfig.class)
@ContextConfiguration(initializers = IntegrationTestContextInitializer.class)
@SpringBootTest(classes = NotificationServiceApp.class)
public class IntegrationEventListenerTest {

    @Value("${spring.kafka.topics.goal-completed-topic.name}")
    private String goalTopic;
    @Value("${spring.kafka.topics.subscription.new-follower-topic.name}")
    private String newFollowerTopic;

    @Autowired
    private KafkaTemplate<String, GoalCompletionNotificationEvent> kafkaTestTemplate;
    @Autowired
    private KafkaTemplate<String, NewFollowerEvent> newFollowerTestTemplate;
    @Autowired
    private KafkaTemplate<String, UnfollowEvent> unfollowEventTestKafkaTemplate;

    @MockBean
    private SmsNotificationService smsService;
    @MockBean
    private EmailNotificationService emailNotificationService;

    private static UserDto correctUserDtoEmail;
    private static UserDto correctUserDtoPhone;

    @BeforeAll
    public static void beforeAll(){
        correctUserDtoEmail = UserDto.builder()
                .id(1L)
                .username("username")
                .email("email")
                .phone("phone")
                .preference(PreferredContact.EMAIL)
                .build();

        correctUserDtoPhone = UserDto.builder()
                .id(2L)
                .username("username")
                .email("email")
                .phone("phone")
                .preference(PreferredContact.PHONE)
                .build();
    }

    @Test
    public void testListenGoalCompletion() {
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        String goalTitle = "test";
        String smsMessage = "\"Congrats! You have achieved test goal!\"";

        GoalCompletionNotificationEvent event = new GoalCompletionNotificationEvent(correctUserDtoPhone, goalTitle);

        when(smsService.getPreferredContact()).thenReturn(PreferredContact.PHONE);

        kafkaTestTemplate.send(goalTopic, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(smsService, times(1)).send(userCaptor.capture(), messageCaptor.capture());

                    UserDto user = userCaptor.getValue();
                    String receivedMsg = messageCaptor.getValue();

                    assertEquals(correctUserDtoPhone.getId(), user.getId());
                    assertEquals(smsMessage, receivedMsg);
                });
    }

    @Test
    public void testNewFollowerEventListener() {
        ArgumentCaptor<UserDto> ownerCaptor = ArgumentCaptor.forClass(UserDto.class);

        NewFollowerEvent event = new NewFollowerEvent(correctUserDtoEmail, correctUserDtoPhone);

        when(emailNotificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        newFollowerTestTemplate.send(newFollowerTopic, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(emailNotificationService, times(1)).send(ownerCaptor.capture(), anyString());

                    UserDto owner = ownerCaptor.getValue();

                    assertNotNull(event);
                    assertEquals(correctUserDtoEmail.getId(), owner.getId());
                });
    }

    @Test
    public void testUnfollowEventListener() {
        ArgumentCaptor<UserDto> ownerCaptor = ArgumentCaptor.forClass(UserDto.class);

        UnfollowEvent event = new UnfollowEvent(correctUserDtoEmail, correctUserDtoPhone);

        when(emailNotificationService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);

        unfollowEventTestKafkaTemplate.send(newFollowerTopic, event);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(emailNotificationService, times(1)).send(ownerCaptor.capture(), anyString());

                    UserDto owner = ownerCaptor.getValue();

                    assertNotNull(event);
                    assertEquals(correctUserDtoEmail.getId(), owner.getId());
                });
    }
}