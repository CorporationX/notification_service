package faang.school.notificationservice.listener.follower;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messaging.follower.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FollowerMessageListenerTest {

    private static final Long FOLLOWEE_ID = 123L;
    private static final Long FOLLOWER_ID = 321L;
    private static final UserDto.PreferredContact PREFERRED_CONTACT = UserDto.PreferredContact.EMAIL;
    private static final String MESSAGE_TEXT = "New follower!";
    private static final String FOLLOWER_USER_NAME = "follower";

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FollowerMessageBuilder followerMessageBuilder;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FollowerMessageListener followerMessageListener;

    private UserDto followeeUser;
    private UserDto followerUser;
    private FollowerEvent followerEvent;
    private Map<UserDto.PreferredContact, NotificationService> notificationServices;

    @BeforeEach
    void setUp() {
        followeeUser = UserDto.builder()
                .preference(PREFERRED_CONTACT)
                .build();

        followerUser = UserDto.builder()
                .username(FOLLOWER_USER_NAME)
                .build();

        followerEvent = FollowerEvent.builder()
                .followerId(FOLLOWER_ID)
                .followeeId(FOLLOWEE_ID)
                .created(LocalDateTime.now())
                .build();

        notificationServices = Map.of(UserDto.PreferredContact.EMAIL, notificationService);

    }

    @Nested
    @DisplayName("When event is successfully processed")
    class WhenEventIsSuccessfullyProcessed {

        @Test
        @DisplayName("Then the notification service should send the message to the followee")
        void whenEventIsSuccessfullyProcessedThenNotificationServiceSendsMessage() throws Exception {
            Message message = mock(Message.class);
            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenReturn(followerEvent);
            when(userService.getUser(FOLLOWEE_ID)).thenReturn(followeeUser);
            when(userService.getUser(FOLLOWER_ID)).thenReturn(followerUser);
            when(followerMessageBuilder.buildMessage(followerUser, Locale.getDefault())).thenReturn(MESSAGE_TEXT);
            when(notificationService.getPreferredContact()).thenReturn(PREFERRED_CONTACT);

            followerMessageListener = new FollowerMessageListener(
                    objectMapper,
                    followerMessageBuilder,
                    notificationServices,
                    userService
            );

            followerMessageListener.onMessage(message, pattern);

            verify(notificationService).send(followeeUser, MESSAGE_TEXT);
        }
    }

    @Nested
    @DisplayName("When deserialization fails")
    class WhenDeserializationFails {

        @Test
        @DisplayName("Then an error should be logged and an exception should be thrown")
        void whenDeserializationFailsThenLogErrorAndThrowException() throws Exception {
            Message message = mock(Message.class);
            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenThrow(new IOException("Invalid data"));

            followerMessageListener = new FollowerMessageListener(objectMapper, followerMessageBuilder, notificationServices, userService);

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    followerMessageListener.onMessage(message, pattern)
            );

            assertEquals("Failed to deserialize event of type: FollowerEvent", exception.getMessage());
            verifyNoInteractions(notificationService);
        }
    }

    @Nested
    @DisplayName("When no matching notification service is found")
    class WhenNoMatchingNotificationServiceIsFound {

        @Test
        @DisplayName("Then a warning should be logged and no notification should be sent")
        void whenNoMatchingNotificationServiceFoundThenLogWarningAndDoNotSendNotification() throws Exception {
            Message message = mock(Message.class);
            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenReturn(followerEvent);
            when(userService.getUser(FOLLOWEE_ID)).thenReturn(followeeUser);
            when(userService.getUser(FOLLOWER_ID)).thenReturn(followerUser);
            when(followerMessageBuilder.buildMessage(followerUser, Locale.getDefault())).thenReturn(MESSAGE_TEXT);

            followerMessageListener = new FollowerMessageListener(objectMapper, followerMessageBuilder, notificationServices, userService);

            followerMessageListener.onMessage(message, pattern);

            verify(notificationService, never()).send(any(), anyString());
        }
    }
}



