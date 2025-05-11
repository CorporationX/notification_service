package faang.school.notificationservice.eventlistener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.subscription.SubscriptionEventDto;
import faang.school.notificationservice.listener.subscription.FollowerEventListener;
import faang.school.notificationservice.messaging.subscription.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @Mock
    private FollowerMessageBuilder messageBuilder;
    @Mock
    private Message message;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    private SubscriptionEventDto eventDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        eventDto = SubscriptionEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .eventTime(LocalDateTime.now())
                .build();

        userDto = UserDto.builder()
                .id(2L)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
    }

    @Test
    void testOnMessage_Success() throws Exception {
        when(message.getBody()).thenReturn("test message".getBytes());
        when(objectMapper.readValue(anyString(), eq(SubscriptionEventDto.class))).thenReturn(eventDto);
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(messageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        followerEventListener = new FollowerEventListener(
                objectMapper,
                userServiceClient,
                List.of(notificationService),
                messageBuilder
        );

        followerEventListener.onMessage(message, null);

        verify(objectMapper).readValue(anyString(), eq(SubscriptionEventDto.class));
        verify(userServiceClient).getUser(2L);
        verify(notificationService).send(eq(userDto), eq("Test message"));
    }

    @Test
    void testBuildMessage() {
        when(messageBuilder.buildMessage(any(), any())).thenReturn("Test message");

        String result = followerEventListener.buildMessage(eventDto, null);
        assertEquals("Test message", result);
    }

    @Test
    void testGetUserId() {
        long userId = followerEventListener.getUserId(eventDto);
        assertEquals(2L, userId);
    }
}