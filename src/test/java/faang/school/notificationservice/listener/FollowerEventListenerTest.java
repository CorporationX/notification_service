package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.messaging.listeners.FollowerEventListener;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    @Mock
    private MessageBuilder<FollowerEvent> followerMessageBuilder;

    private FollowerEventListener listener;

    private UserDto followedUser;
    private FollowerEvent followerEvent;

    @BeforeEach
    void setUp() {
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(smsNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);
        when(followerMessageBuilder.getInstance()).thenReturn((Class) FollowerEvent.class);

        List<NotificationService> notificationServices = List.of(emailNotificationService, smsNotificationService);
        List<MessageBuilder<?>> messageBuilders = List.of(followerMessageBuilder);

        listener = new FollowerEventListener(
                objectMapper,
                userService,
                notificationServices,
                messageBuilders
        );

        followedUser = UserDto.builder()
                .id(100L)
                .username("followeduser")
                .email("followed@example.com")
                .phone("+1234567890")
                .locale(Locale.ENGLISH)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        followerEvent = new FollowerEvent(50L, 100L, LocalDateTime.now(), Locale.ENGLISH);
    }

    @Test
    void onMessage_shouldProcessEventAndSendNotification() {
        String expectedMessage = "Congrats! You've got a new follower!";
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(eq(followerEvent), eq(Locale.ENGLISH)))
                .thenReturn(expectedMessage);

        listener.onMessage(followerEvent);

        verify(userService, times(2)).getUser(100L);
        verify(followerMessageBuilder).buildMessage(followerEvent, Locale.ENGLISH);
        verify(emailNotificationService).send(followedUser, expectedMessage);
    }

    @Test
    void onMessage_shouldUseFollowedUserLocale() {
        followedUser.setLocale(Locale.GERMAN);
        String expectedMessage = "Glückwunsch! Du hast einen neuen Follower!";
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(eq(followerEvent), eq(Locale.GERMAN)))
                .thenReturn(expectedMessage);

        listener.onMessage(followerEvent);

        verify(followerMessageBuilder).buildMessage(followerEvent, Locale.GERMAN);
        verify(emailNotificationService).send(followedUser, expectedMessage);
    }

    @Test
    void onMessage_shouldUseSmsService_whenUserPrefersSms() {
        followedUser.setPreference(UserDto.PreferredContact.SMS);
        String expectedMessage = "Congrats! You've got a new follower!";
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        listener.onMessage(followerEvent);

        verify(smsNotificationService).send(followedUser, expectedMessage);
        verify(emailNotificationService, never()).send(any(), any());
    }

    @Test
    void onMessage_shouldNotSendToFollower() {
        String expectedMessage = "Congrats! You've got a new follower!";
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        listener.onMessage(followerEvent);

        verify(userService, never()).getUser(followerEvent.followerId());
    }

    @Test
    void onMessage_shouldProcessMultipleEventsIndependently() {
        FollowerEvent event1 = new FollowerEvent(50L, 100L, LocalDateTime.now(), Locale.ENGLISH);
        FollowerEvent event2 = new FollowerEvent(60L, 100L, LocalDateTime.now(), Locale.ENGLISH);

        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        listener.onMessage(event1);
        listener.onMessage(event2);

        verify(emailNotificationService, times(2)).send(eq(followedUser), anyString());
    }

    @Test
    void onMessage_shouldHandleDifferentPreferences() {
        UserDto user1 = UserDto.builder()
                .id(100L)
                .username("user1")
                .email("user1@example.com")
                .locale(Locale.ENGLISH)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        UserDto user2 = UserDto.builder()
                .id(200L)
                .username("user2")
                .email("user2@example.com")
                .locale(Locale.ENGLISH)
                .preference(UserDto.PreferredContact.SMS)
                .build();

        FollowerEvent event1 = new FollowerEvent(50L, 100L, LocalDateTime.now(), Locale.ENGLISH);
        FollowerEvent event2 = new FollowerEvent(60L, 200L, LocalDateTime.now(), Locale.ENGLISH);

        when(userService.getUser(100L)).thenReturn(user1);
        when(userService.getUser(200L)).thenReturn(user2);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn("Test");

        listener.onMessage(event1);
        listener.onMessage(event2);

        verify(emailNotificationService).send(eq(user1), anyString());
        verify(smsNotificationService).send(eq(user2), anyString());
    }
}