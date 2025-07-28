package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.notification.AggregatedNotificationsDto;
import faang.school.notificationservice.enums.PreferredContact;
import faang.school.notificationservice.messaging.like.CommentLikedEventMessageBuilder;
import faang.school.notificationservice.messaging.like.PostLikedEventMessageBuilder;
import faang.school.notificationservice.repository.NotificationRepository;
import faang.school.notificationservice.service.notification.EventType;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationSenderServiceTest {

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private CommentLikedEventMessageBuilder commentMessageBuilder;

    @Mock
    private PostLikedEventMessageBuilder postMessageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationSenderService notificationSenderService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        testUser = UserDto.builder()
                .id(42L)
                .preference(PreferredContact.EMAIL)
                .locale(Locale.UK)
                .build();
    }

    @Test
    void testWhenUsedCorrectNotificationService() {
        NotificationService emailService = mock(NotificationService.class);
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(notificationServices.stream()).thenReturn(Stream.of(emailService));

        notificationSenderService.send(testUser, "Notification message");

        verify(emailService, times(1)).send(testUser, "Notification message");
    }

    @Test
    void testWhenUsedWrongNotificationService() {
        NotificationService smsService = mock(NotificationService.class);
        when(smsService.getPreferredContact()).thenReturn(PreferredContact.PHONE);
        when(notificationServices.stream()).thenReturn(Stream.of(smsService));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> notificationSenderService.send(testUser, "Notification message"),
                "Expected to throw because no service matches the preference"
        );

        assertEquals("No notification service was found for user preferred notification type", thrown.getMessage());
    }

    @Test
    void testNotificationSenderWithCommentLikedEventType() {
        AggregatedNotificationsDto notification = AggregatedNotificationsDto.builder()
                .receiverId(42L)
                .targetEntityId(123L)
                .eventType(EventType.COMMENT_LIKED)
                .build();

        NotificationService emailService = mock(NotificationService.class);
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(notificationServices.stream()).thenReturn(Stream.of(emailService));
        when(userServiceClient.getUser(42L)).thenReturn(testUser);
        when(commentMessageBuilder.buildMessage(eq(notification), any(Locale.class))).thenReturn("Comment Liked Message");

        notificationSenderService.sendAggregatedNotifications(notification);

        verify(userServiceClient, times(1)).getUser(42L);
        verify(commentMessageBuilder, times(1)).buildMessage(eq(notification), eq(Locale.UK));
        verify(notificationRepository, times(1)).updateStatusByGroup(
                42L,
                123L,
                "COMMENT_LIKED",
                "SENT"
        );
    }


    @Test
    void testNotificationSenderWithPostLikedEventType() {
        AggregatedNotificationsDto notification = AggregatedNotificationsDto.builder()
                .receiverId(42L)
                .targetEntityId(456L)
                .eventType(EventType.POST_LIKED)
                .build();

        NotificationService emailService = mock(NotificationService.class);
        when(emailService.getPreferredContact()).thenReturn(PreferredContact.EMAIL);
        when(notificationServices.stream()).thenReturn(Stream.of(emailService));
        when(userServiceClient.getUser(42L)).thenReturn(testUser);
        when(postMessageBuilder.buildMessage(eq(notification), any(Locale.class)))
                .thenReturn("Post Liked Message");

        notificationSenderService.sendAggregatedNotifications(notification);

        verify(userServiceClient, times(1)).getUser(42L);
        verify(postMessageBuilder, times(1)).buildMessage(eq(notification), eq(Locale.UK));
        verify(notificationRepository, times(1)).updateStatusByGroup(
                42L,
                456L,
                "POST_LIKED",
                "SENT"
        );
    }

    @Test
    void testNotificationStatusUpdateWhenFailed() {
        AggregatedNotificationsDto notification = AggregatedNotificationsDto.builder()
                .receiverId(42L)
                .targetEntityId(456L)
                .eventType(EventType.POST_LIKED)
                .build();
        when(userServiceClient.getUser(42L)).thenReturn(testUser);
        when(postMessageBuilder.buildMessage(eq(notification), any(Locale.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        notificationSenderService.sendAggregatedNotifications(notification);

        verify(userServiceClient, times(1)).getUser(42L);
        verify(postMessageBuilder, times(1)).buildMessage(eq(notification), eq(Locale.UK));
        verify(notificationRepository, times(1)).updateStatusByGroup(
                42L,
                456L,
                "POST_LIKED",
                "FAILED"
        );
    }
}