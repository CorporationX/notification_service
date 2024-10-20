package faang.school.notificationservice.listener.follower;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.follower.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FollowerMessageListenerTest {

    private static final Long FOLLOWEE_ID = 123L;
    private static final Long FOLLOWER_ID = 321L;
    private static final UserDto.PreferredContact PREFERRED_CONTACT = UserDto.PreferredContact.EMAIL;
    private static final String MESSAGE_TEXT = "New follower!";

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FollowerMessageBuilder followerMessageBuilder;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FollowerEventListener followerMessageListener;

    private UserDto followeeUser;
    private FollowerEvent followerEvent;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices = new HashMap<>();
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();

    @BeforeEach
    void setUp() {
        followeeUser = UserDto.builder()
                .notifyPreference(PREFERRED_CONTACT)
                .build();

        followerEvent = FollowerEvent.builder()
                .followerId(FOLLOWER_ID)
                .followeeId(FOLLOWEE_ID)
                .created(LocalDateTime.now())
                .build();

        followerMessageListener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                messageBuilders,
                notificationServices
        );
    }

    @Nested
    @DisplayName("When event is successfully processed")
    class WhenEventIsSuccessfullyProcessed {

        @Test
        @DisplayName("Then the notification service should send the message to the followee")
        void whenEventIsSuccessfullyProcessedThenNotificationServiceSendsMessage() throws Exception {
            messageBuilders.put(followerEvent.getClass(), followerMessageBuilder);
            notificationServices.put(PREFERRED_CONTACT, notificationService);

            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenReturn(followerEvent);
            when(userServiceClient.getUser(FOLLOWEE_ID)).thenReturn(followeeUser);
            when(followerMessageBuilder.buildMessage(followerEvent, Locale.ENGLISH)).thenReturn(MESSAGE_TEXT);

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
            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenThrow(new IOException("Invalid data"));

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    followerMessageListener.onMessage(message, pattern)
            );

            assertEquals("java.io.IOException: Invalid data", exception.getMessage());
            verifyNoInteractions(notificationService);
        }
    }

    @Nested
    @DisplayName("When no matching notification service is found")
    class WhenNoMatchingNotificationServiceIsFound {

        @Test
        @DisplayName("Then a warning should be logged and no notification should be sent")
        void whenNoMatchingNotificationServiceFoundThenLogWarningAndDoNotSendNotification() throws Exception {
            messageBuilders.put(followerEvent.getClass(), followerMessageBuilder);
            byte[] pattern = new byte[0];

            when(message.getBody()).thenReturn(pattern);
            when(objectMapper.readValue(any(byte[].class), eq(FollowerEvent.class))).thenReturn(followerEvent);
            when(userServiceClient.getUser(FOLLOWEE_ID)).thenReturn(followeeUser);
            when(followerMessageBuilder.buildMessage(followerEvent, Locale.ENGLISH)).thenReturn(MESSAGE_TEXT);

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    followerMessageListener.onMessage(message, pattern)
            );

            assertEquals("Not found notification service", exception.getMessage());
            verify(notificationService, never()).send(any(), anyString());
        }
    }
}



