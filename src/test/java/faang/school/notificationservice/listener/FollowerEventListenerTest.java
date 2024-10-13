package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Spy
    private List<NotificationService> notificationServices;

    @Spy
    private List<MessageBuilder<FollowerEvent>> messageBuilders;

    @Spy
    @InjectMocks
    private FollowerEventListener followerEventListener;

    private byte[] messageValue;
    private Message message;

    @BeforeEach
    void setUp() {
        messageValue = new byte[]{1, 2, 3};
        message = mock(Message.class);

        when(message.getBody()).thenReturn(messageValue);
    }

    @Test
    void onMessage_ShouldProcessMessageAndSendNotification() throws IOException {
        String notificationMessage = "Notification message";
        long followeeId = 1L;
        FollowerEvent event = new FollowerEvent();
        event.setFolloweeId(followeeId);
        UserDto followee = new UserDto();
        followee.setId(followeeId);

        when(objectMapper.readValue(messageValue, FollowerEvent.class)).thenReturn(event);
        when(userServiceClient.tryGetUser(followeeId)).thenReturn(followee);
        doReturn(notificationMessage).when(followerEventListener).getMessage(followee, event);
        doNothing().when(followerEventListener).sendNotification(followee, notificationMessage);

        followerEventListener.onMessage(message, null);

        verify(objectMapper).readValue(messageValue, FollowerEvent.class);
        verify(userServiceClient).tryGetUser(followeeId);
        verify(followerEventListener).sendNotification(followee, notificationMessage);
    }

    @Test
    void onMessage_ShouldLogErrorWhenMessageCannotBeDeserialized() throws IOException {
        when(objectMapper.readValue(messageValue, FollowerEvent.class))
                .thenThrow(new IOException("Deserialization error"));

        followerEventListener.onMessage(message, null);

        verify(objectMapper).readValue(messageValue, FollowerEvent.class);
        verify(userServiceClient, never()).tryGetUser(anyLong());
        verify(followerEventListener, never()).getMessage(any(UserDto.class), any(FollowerEvent.class));
        verify(followerEventListener, never()).sendNotification(any(UserDto.class), anyString());
    }
}
