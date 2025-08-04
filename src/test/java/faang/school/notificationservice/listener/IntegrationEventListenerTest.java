package faang.school.notificationservice.listener;

import faang.school.notificationservice.NotificationServiceApp;
import faang.school.notificationservice.config.IntegrationTestContextInitializer;
import faang.school.notificationservice.config.TestKafkaConfig;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.event.kafka.NewFollowerEvent;
import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.event.kafka.UnfollowEvent;
import faang.school.notificationservice.service.notification.handler.CommentLikedNotificationEventHandler;
import faang.school.notificationservice.service.notification.handler.PostLikedNotificationEventHandler;
import faang.school.notificationservice.service.notification.implimentation.EmailNotificationService;
import faang.school.notificationservice.service.notification.implimentation.SmsNotificationService;
import faang.school.notificationservice.service.notification.implimentation.TelegramNotificationService;
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
import static org.mockito.ArgumentMatchers.argThat;
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
    @Value("${spring.kafka.topics.comment-created-topic.name}")
    private String commentCreatedTopic;
    @Value("${spring.kafka.topics.comment-liked-topic.name}")
    private String commentLikedTopic;
    @Value("${spring.kafka.topics.post-liked-topic.name}")
    private String postLikedTopic;

    @Autowired
    private KafkaTemplate<String, GoalCompletionNotificationEvent> kafkaTestTemplate;
    @Autowired
    private KafkaTemplate<String, NewFollowerEvent> newFollowerTestTemplate;
    @Autowired
    private KafkaTemplate<String, UnfollowEvent> unfollowEventTestKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, CommentCreationNotificationEvent> commentCreatedTestKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, CommentLikedNotificationEvent> commentLikedTestKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, PostLikedNotificationEvent> postLikedTestKafkaTemplate;
    @Autowired
    private CommentLikedNotificationEventHandler commentLikedNotificationEventHandler;
    @Autowired
    private PostLikedNotificationEventHandler postLikedNotificationEventHandler;

    @MockBean
    private SmsNotificationService smsService;
    @MockBean
    private EmailNotificationService emailNotificationService;
    @MockBean
    private TelegramNotificationService telegramNotificationService;

    private static UserDto correctUserDtoEmail;
    private static UserDto correctUserDtoPhone;
    private static UserDto correctUserDtoTelegram;

    @BeforeAll
    public static void beforeAll() {
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

        correctUserDtoTelegram = UserDto.builder()
                .id(3L)
                .username("username")
                .email("email")
                .phone("phone")
                .preference(PreferredContact.TELEGRAM)
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

    @Test
    public void testCommentCreationEventListenerWithTelegram() {
        ArgumentCaptor<UserDto> ownerCaptor = ArgumentCaptor.forClass(UserDto.class);
        CommentCreationNotificationEvent commentCreationEvent = CommentCreationNotificationEvent.builder()
                .owner(correctUserDtoTelegram)
                .commentAuthorUserName("username")
                .shortContent("short content")
                .build();

        when(telegramNotificationService.getPreferredContact()).thenReturn(PreferredContact.TELEGRAM);
        commentCreatedTestKafkaTemplate.send(commentCreatedTopic, commentCreationEvent);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    verify(telegramNotificationService, times(1)).send(ownerCaptor.capture(), anyString());
                    UserDto owner = ownerCaptor.getValue();
                    assertNotNull(commentCreationEvent);
                    assertEquals(correctUserDtoTelegram.getId(), owner.getId());
                });
    }

    @Test
    public void testHandleCommentLikedNotificationEventIntegration() {
        CommentLikedNotificationEvent commentLikedEvent = CommentLikedNotificationEvent.builder()
                .commentId(202L)
                .owner(correctUserDtoEmail)
                .commentId(10L)
                .build();

        commentLikedTestKafkaTemplate.send(commentLikedTopic, commentLikedEvent);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> verify(commentLikedNotificationEventHandler, times(1))
                        .saveNotifications(argThat(events ->
                                events.stream().anyMatch(event ->
                                        event.getCommentId().equals(202L) &&
                                                event.getCommentId().equals(10L) &&
                                                event.getOwner().equals(correctUserDtoPhone)
                                )
                        ))
                );
    }

    @Test
    public void testHandlePostLikedNotificationEventIntegration() {
        PostLikedNotificationEvent postLikedEvent = PostLikedNotificationEvent.builder()
                .postId(20L)
                .owner(correctUserDtoEmail)
                .likerUsername("some_user")
                .shortContent("test comment")
                .build();

        postLikedTestKafkaTemplate.send(postLikedTopic, postLikedEvent);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> verify(postLikedNotificationEventHandler, times(1))
                        .saveNotifications(argThat(events ->
                                events.stream().anyMatch(event ->
                                        event.getPostId().equals(20L) &&
                                                event.getOwner().equals(correctUserDtoTelegram)
                                )
                        ))
                );
    }
}