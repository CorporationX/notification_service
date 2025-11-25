package faang.school.notificationservice.listener;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.listeners.FollowerEventListener;
import faang.school.notificationservice.messaging.listeners.FollowerEventListener.FollowerEvent;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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

    @Mock
    private Message redisMessage;

    private FollowerEventListener listener;

    private UserDto followedUser;
    private FollowerEvent followerEvent;
    private byte[] messageBody;

    @BeforeEach
    void setUp() throws Exception {
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

        followerEvent = new FollowerEvent(50L, 100L);
        messageBody = "{\"followerId\":50,\"followedUserId\":100}".getBytes();
    }

    @Test
    void onMessage_shouldProcessEventAndSendNotification() throws Exception {
        // Arrange
        String expectedMessage = "Congrats! You've got a new follower!";
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(eq(followerEvent), eq(Locale.ENGLISH)))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(objectMapper).readValue(messageBody, FollowerEvent.class);
        verify(userService, times(2)).getUser(100L); // Once for locale, once for sending
        verify(followerMessageBuilder).buildMessage(followerEvent, Locale.ENGLISH);
        verify(emailNotificationService).send(followedUser, expectedMessage);
    }

    @Test
    void onMessage_shouldUseFollowedUserLocale() throws Exception {
        // Arrange
        followedUser.setLocale(Locale.GERMAN);
        String expectedMessage = "Glückwunsch! Du hast einen neuen Follower!";
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(eq(followerEvent), eq(Locale.GERMAN)))
                .thenReturn(expectedMessage);

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(followerMessageBuilder).buildMessage(followerEvent, Locale.GERMAN);
        verify(emailNotificationService).send(followedUser, expectedMessage);
    }

    @Test
    void onMessage_shouldUseSmsService_whenUserPrefersSms() throws Exception {
        // Arrange
        followedUser.setPreference(UserDto.PreferredContact.SMS);
        String expectedMessage = "Congrats! You've got a new follower!";
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(smsNotificationService).send(followedUser, expectedMessage);
        verify(emailNotificationService, never()).send(any(), any());
    }

    @Test
    void onMessage_shouldSendNotificationToFollowedUser() throws Exception {
        // Arrange
        String expectedMessage = "Congrats! You've got a new follower!";
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(userService, times(2)).getUser(followerEvent.followedUserId());
        verify(emailNotificationService).send(followedUser, expectedMessage);
    }

    @Test
    void onMessage_shouldNotSendToFollower() throws Exception {
        // Arrange
        String expectedMessage = "Congrats! You've got a new follower!";
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(userService, never()).getUser(followerEvent.followerId());
    }

    @Test
    void onMessage_shouldHandleJsonParsingException() throws Exception {
        // Arrange
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class))
                .thenThrow(new RuntimeException("JSON parsing error"));

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(objectMapper).readValue(messageBody, FollowerEvent.class);
        verify(userService, never()).getUser(anyLong());
        verify(emailNotificationService, never()).send(any(), any());
        verify(smsNotificationService, never()).send(any(), any());
    }

    @Test
    void onMessage_shouldHandleUserServiceException() throws Exception {
        // Arrange
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);
        when(userService.getUser(100L)).thenThrow(new RuntimeException("User not found"));

        // Act
        listener.onMessage(redisMessage, null);

        // Assert
        verify(userService).getUser(100L);
        verify(emailNotificationService, never()).send(any(), any());
        verify(smsNotificationService, never()).send(any(), any());
    }

    @Test
    void onMessage_shouldProcessMultipleEventsIndependently() throws Exception {
        // Arrange
        FollowerEvent event1 = new FollowerEvent(50L, 100L);
        FollowerEvent event2 = new FollowerEvent(60L, 100L);

        byte[] messageBody1 = "{\"followerId\":50,\"followedUserId\":100}".getBytes();
        byte[] messageBody2 = "{\"followerId\":60,\"followedUserId\":100}".getBytes();

        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);

        when(message1.getBody()).thenReturn(messageBody1);
        when(message2.getBody()).thenReturn(messageBody2);

        when(objectMapper.readValue(messageBody1, FollowerEvent.class)).thenReturn(event1);
        when(objectMapper.readValue(messageBody2, FollowerEvent.class)).thenReturn(event2);

        when(userService.getUser(100L)).thenReturn(followedUser);
        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        // Act
        listener.onMessage(message1, null);
        listener.onMessage(message2, null);

        // Assert
        verify(objectMapper).readValue(messageBody1, FollowerEvent.class);
        verify(objectMapper).readValue(messageBody2, FollowerEvent.class);
        verify(emailNotificationService, times(2)).send(eq(followedUser), anyString());
    }

    @Test
    void onMessage_shouldHandleDifferentPreferences() throws Exception {
        // Arrange
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

        FollowerEvent event1 = new FollowerEvent(50L, 100L);
        FollowerEvent event2 = new FollowerEvent(60L, 200L);

        byte[] body1 = "{\"followerId\":50,\"followedUserId\":100}".getBytes();
        byte[] body2 = "{\"followerId\":60,\"followedUserId\":200}".getBytes();

        Message msg1 = mock(Message.class);
        Message msg2 = mock(Message.class);

        when(msg1.getBody()).thenReturn(body1);
        when(msg2.getBody()).thenReturn(body2);

        when(objectMapper.readValue(body1, FollowerEvent.class)).thenReturn(event1);
        when(objectMapper.readValue(body2, FollowerEvent.class)).thenReturn(event2);

        when(userService.getUser(100L)).thenReturn(user1);
        when(userService.getUser(200L)).thenReturn(user2);

        when(followerMessageBuilder.buildMessage(any(), any())).thenReturn("Test");

        // Act
        listener.onMessage(msg1, null);
        listener.onMessage(msg2, null);

        // Assert
        verify(emailNotificationService).send(eq(user1), anyString());
        verify(smsNotificationService).send(eq(user2), anyString());
    }
}